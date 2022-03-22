package com.company;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ByteStreamIterator implements Iterator<byte[]>, Closeable {

    private final InputStream inputStream;
    private final int bufferSize;
    private long contentSize;
    private byte[] buffer;

    public ByteStreamIterator(InputStream inputStream, int bufferSize, long contentSize) {
        this.inputStream = inputStream;
        this.bufferSize = bufferSize;
        this.contentSize = contentSize;
    }

    @Override
    public boolean hasNext() {
        return contentSize > 0;
    }

    @Override
    public byte[] next() {
        if(!hasNext()) return null;

        buffer = new byte[(int) Math.min(contentSize, bufferSize)];
        try {
            int processed = inputStream.read(buffer, 0, buffer.length);
            contentSize = contentSize - processed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}