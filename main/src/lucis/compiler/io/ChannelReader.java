package lucis.compiler.io;

import lucis.compiler.entity.Position;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class ChannelReader implements Reader {
    private final int bufferSize;
    private ByteBuffer rawDataBuffer = null;
    private CharBuffer decodeDataBuffer = null;
    private final CharsetDecoder decoder;
    private final ReadableByteChannel channel;
    private Queue<Integer> markingCache = null;
    private Queue<Integer> markedCache = null;
    private boolean marked = false;
    private Position markedPosition = null;
    private long line = 1;
    private long offset = 1;

    public ChannelReader(ReadableByteChannel channel) {
        this(channel, 1024);
    }

    public ChannelReader(ReadableByteChannel channel, int bufferSize) {
        this(channel, bufferSize, StandardCharsets.UTF_8);
    }

    public ChannelReader(ReadableByteChannel channel, int bufferSize, Charset charset) {
        Objects.requireNonNull(channel, "the channel to be read by the reader cannot be null");
        if (bufferSize <= 0) throw new IllegalArgumentException("the buffer size of the reader must be positive");
        Objects.requireNonNull(charset, "the charset of the reader cannot be null");
        this.channel = channel;
        this.bufferSize = bufferSize;
        this.decoder = charset.newDecoder();
    }

    private Character readUTF16() throws IOException {
        if (rawDataBuffer == null) rawDataBuffer = ByteBuffer.allocate(bufferSize);
        while (decodeDataBuffer == null || !decodeDataBuffer.hasRemaining()) {
            if (channel.read(rawDataBuffer) == -1) {
                return null;
            }
            rawDataBuffer.flip();
            decodeDataBuffer = decoder.decode(rawDataBuffer);
            rawDataBuffer.compact();
        }
        return decodeDataBuffer.get();
    }

    private Integer readUnicode() throws IOException {
        if (markedCache != null && !markedCache.isEmpty()) return markedCache.poll();
        Character c = readUTF16();
        if (c == null) return null;
        int unicode;
        if (Character.isSurrogate(c)) {
            Character c_ = readUTF16();
            if (c_ == null) throw new IOException("expect more data to form a surrogate pair");
            if (!Character.isSurrogatePair(c, c_))
                throw new IOException("expect a surrogate pair, but get " + c + " and " + c_ + " instead");
            unicode = Character.toCodePoint(c, c_);
        } else unicode = (int) c;
        return unicode;
    }

    @Override
    public Integer next() throws IOException {
        Integer unicode = readUnicode();
        if (unicode == null) return unicode;
        if (unicode == '\n') {
            ++line;
            offset = 1;
        } else ++offset;
        if (marked) markingCache.offer(unicode);
        return unicode;
    }

    @Override
    public Integer peek() throws IOException {
        if (markedCache != null && !markedCache.isEmpty()) return markedCache.peek();
        Integer unicode = readUnicode();
        if (unicode == null) return unicode;
        if (markedCache == null) markedCache = new LinkedList<>();
        markedCache.offer(unicode);
        return unicode;
    }

    @Override
    public boolean skip() throws IOException {
        return next() != null;
    }

    @Override
    public Position position() {
        return new Position(line, offset);
    }

    @Override
    public void mark() {
        marked = true;
        markingCache = new LinkedList<>();
        markedPosition = position();
    }

    @Override
    public void reset() {
        marked = false;
        if (markedCache != null && markingCache != null)
            markedCache.forEach(markingCache::offer);
        markedCache = markingCache;
        if (markedPosition != null) {
            line = markedPosition.line();
            offset = markedPosition.offset();
        }
    }
}
