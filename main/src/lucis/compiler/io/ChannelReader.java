package lucis.compiler.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ChannelReader implements Reader {
    private int bufferSize;
    private long position = 0;
    private boolean available = true;
    private ByteBuffer rawDataBuffer = null;
    private CharBuffer cacheDataBuffer = null;
    private CharBuffer decodeDataBuffer = null;
    private CharsetDecoder decoder;
    private ReadableByteChannel channel;

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

    @Override
    public Character get() throws IOException {
        if (!available()) return null;
        if (cacheDataBuffer != null && cacheDataBuffer.hasRemaining()) return cacheDataBuffer.get();
        if (rawDataBuffer == null) rawDataBuffer = ByteBuffer.allocate(bufferSize);
        while (decodeDataBuffer == null || !decodeDataBuffer.hasRemaining()) {
            if (channel.read(rawDataBuffer) == -1) {
                available = false;
                return null;
            }
            rawDataBuffer.flip();
            decodeDataBuffer = decoder.decode(rawDataBuffer);
            rawDataBuffer.compact();
        }
        ++position;
        return decodeDataBuffer.get();
    }

    @Override
    public void put(Character c) {
        Objects.requireNonNull(c, "the character to be put to the reader cannot be null");
        if (cacheDataBuffer == null) {
            cacheDataBuffer = CharBuffer.allocate(bufferSize);
            cacheDataBuffer.position(cacheDataBuffer.limit());
        }
        cacheDataBuffer.put(cacheDataBuffer.position() - 1, c);
        cacheDataBuffer.position(cacheDataBuffer.position() - 1);
        --position;
        available = true;
    }

    @Override
    public boolean skip() throws IOException {
        return get() != null;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public boolean available() {
        return available;
    }
}
