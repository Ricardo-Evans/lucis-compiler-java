package lucis.compiler.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
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
    private final Queue<Integer> cache = new LinkedList<>();
    private Integer peek = null;
    private boolean marked = false;

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

    private Character read() throws IOException {
        if (rawDataBuffer == null) rawDataBuffer = ByteBuffer.allocate(bufferSize);
        while (decodeDataBuffer == null || !decodeDataBuffer.hasRemaining()) {
            if (channel.read(rawDataBuffer) == -1) return null;
            rawDataBuffer.flip();
            decodeDataBuffer = decoder.decode(rawDataBuffer);
            rawDataBuffer.compact();
        }
        return decodeDataBuffer.get();
    }

    @Override
    public Integer next() throws IOException {
        if (peek != null) {
            Integer peek = this.peek;
            this.peek = null;
            return peek;
        }
        if (!marked && !cache.isEmpty()) return cache.poll();
        Character c = read();
        if (c == null) return null;
        int integer;
        if (Character.isSurrogate(c)) {
            Character c_ = read();
            if (c_ == null) throw new IOException("expect more data to form a surrogate pair");
            if (!Character.isSurrogatePair(c, c_))
                throw new IOException("expect a surrogate pair, but get " + c + " and " + c_ + " instead");
            integer = Character.toCodePoint(c, c_);
        } else integer = (int) c;
        if (marked) cache.offer(integer);
        return integer;
    }

    @Override
    public Integer peek() throws IOException {
        if (peek != null) return peek;
        return peek = next();
    }

    @Override
    public boolean skip() throws IOException {
        return next() != null;
    }

    @Override
    public void mark() {
        marked = true;
        cache.clear();
    }

    @Override
    public void reset() {
        marked = false;
    }
}
