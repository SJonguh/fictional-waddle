package com.company;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileReadHandler extends FileHandler {

    public FileReadHandler(File file) {
        super(file);
    }

    public FileReadHandler(File file, int bufferSize) {
        super(file, bufferSize);
    }

    public FileStreamIterator getIterator() throws IOException {
        return getIterator(bufferSize);
    }

    public FileStreamIterator getIterator(int bufferSize) throws IOException {
        return new FileStreamIterator(file, bufferSize);
    }

    public String getChecksum(String digest) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(digest);
        try(FileStreamIterator fileStreamIterator = getIterator()) {
            while (fileStreamIterator.hasNext()) {
                messageDigest.update(fileStreamIterator.next());
            }
            return checksum(messageDigest);
        }
    }
}