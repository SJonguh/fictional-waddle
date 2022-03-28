package io.disk;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

public class FileStreamIterator implements Iterator<byte[]>, Closeable {
    private final FileInputStream fileInputStream;
    private final FileChannel fileChannel;
    private final MappedByteBuffer[] mappedByteBuffers;
    private final long fileSize;
    private final int bufferSize;
    private final int mappedBuffers;

    private int count;
    private byte[] buffer;

    public FileStreamIterator(File file, int bufferSize) throws IOException {
        this.fileInputStream = new FileInputStream(file);
        this.bufferSize = bufferSize;
        this.fileChannel = fileInputStream.getChannel();
        this.fileSize = fileChannel.size();
        this.mappedBuffers = (int) (fileSize / Integer.MAX_VALUE) + 1;
        this.mappedByteBuffers = new MappedByteBuffer[mappedBuffers];
        mapByteBuffers();
    }

    private void mapByteBuffers() throws IOException {
        long position = 0;
        long bytes = Integer.MAX_VALUE;
        for (int i = 0; i < mappedBuffers; i++) {
            if (fileSize - position < Integer.MAX_VALUE) {
                bytes = fileSize - position;
            }
            mappedByteBuffers[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, bytes);
            position += bytes;
        }
    }

    @Override
    public boolean hasNext() {
        return count < mappedBuffers;
    }

    @Override
    public byte[] next() {
        if (!hasNext()) return null;

        MappedByteBuffer mappedByteBuffer = mappedByteBuffers[count];

        int limit = mappedByteBuffer.limit();
        int position = mappedByteBuffer.position();

        buffer = new byte[bufferSize];
        if (limit - position < bufferSize) {
            buffer = new byte[limit - position];
            count++;
        }
        mappedByteBuffer.get(buffer);
        return buffer;
    }

    @Override
    public void close() throws IOException {
        fileInputStream.close();
        buffer = null;
    }
}