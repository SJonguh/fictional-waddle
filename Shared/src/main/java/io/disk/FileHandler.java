package io.disk;

import java.io.File;
import java.security.MessageDigest;

public abstract class FileHandler {
    protected final File file;
    protected final int bufferSize;

    public FileHandler(File file) {
        this(file, Short.MAX_VALUE);
    }

    public FileHandler(File file, int bufferSize) {
        this.file = file;
        this.bufferSize = bufferSize;
    }

    protected String checksum(MessageDigest messageDigest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}