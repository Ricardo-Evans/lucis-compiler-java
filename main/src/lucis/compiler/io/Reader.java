package lucis.compiler.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Reader {
    private int buffer;
    private long position = 0;
    private boolean available = true;
    private ByteBuffer rawDataBuffer = null;
    private CharBuffer cacheDataBuffer = null;
    private CharBuffer decodeDataBuffer = null;
    private CharsetDecoder decoder;
    private ReadableByteChannel channel;

    public Reader(ReadableByteChannel channel) {
        this(channel, 1024);
    }

    public Reader(ReadableByteChannel channel, int buffer) {
        this(channel, buffer, StandardCharsets.UTF_8);
    }

    public Reader(ReadableByteChannel channel, int buffer, Charset charset) {
        Objects.requireNonNull(channel, "the channel to be read by the reader cannot be null");
        if (buffer <= 0) throw new IllegalArgumentException("the buffer size of the reader must be positive");
        Objects.requireNonNull(charset, "the charset of the reader cannot be null");
        this.channel = channel;
        this.buffer = buffer;
        this.decoder = charset.newDecoder();
    }

    public Character get() throws IOException {
        Character c = read();
        if (c != null) ++position;
        return c;
    }

    private Character read() throws IOException {
        if (!available()) return null;
        if (cacheDataBuffer != null && cacheDataBuffer.hasRemaining()) return cacheDataBuffer.get();
        if (rawDataBuffer == null) rawDataBuffer = ByteBuffer.allocate(buffer);
        while (decodeDataBuffer == null || !decodeDataBuffer.hasRemaining()) {
            if (channel.read(rawDataBuffer) == -1) {
                available = false;
                return null;
            }
            rawDataBuffer.flip();
            decodeDataBuffer = decoder.decode(rawDataBuffer);
            rawDataBuffer.compact();
        }
        return decodeDataBuffer.get();
    }

    public void put(Character c) {
        Objects.requireNonNull(c, "the character to be put to the reader cannot be null");
        if (cacheDataBuffer == null) {
            cacheDataBuffer = CharBuffer.allocate(buffer);
            cacheDataBuffer.position(cacheDataBuffer.limit());
        }
        cacheDataBuffer.put(cacheDataBuffer.position() - 1, c);
        cacheDataBuffer.position(cacheDataBuffer.position() - 1);
        --position;
        available = true;
    }

    public boolean skip() throws IOException {
        return get() != null;
    }

    public long position(){
        return position;
    }

    public boolean available() {
        return available;
    }
}
