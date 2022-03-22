package com.company;

import java.io.*;
import java.nio.file.FileSystemException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

public class FileWriteHandler extends FileHandler {

    public FileWriteHandler(File file) {
        super(file);
    }

    public FileWriteHandler(File file, int bufferSize) {
        super(file, bufferSize);
    }

    public boolean write(InputStream inputStream, long fileSize, String checksum, String digest) throws IOException, NoSuchAlgorithmException {
        ByteStreamIterator byteStreamIterator = new ByteStreamIterator(inputStream, bufferSize, fileSize);
        return write(byteStreamIterator, fileSize, checksum, digest);
    }

    public boolean write(Iterator<byte[]> iterator, long fileSize, String checksum, String digest) throws IOException, NoSuchAlgorithmException {
        createParentDirectoryIfNotExist();

        MessageDigest messageDigest = MessageDigest.getInstance(digest);
        try (FileOutputStream out = new FileOutputStream(file)) {
            while(iterator.hasNext()) {
                byte[] buffer = iterator.next();
                out.write(buffer);
                messageDigest.update(buffer);
            }

            if(checksum(messageDigest).equals(checksum)) {
                return true;
            }
        }
        return false;
    }

    private void createParentDirectoryIfNotExist() throws FileSystemException {
        if (!file.exists()) {
            File parent = new File(file.getParent());
            if (!parent.mkdirs() && !parent.isDirectory()) {
                throw new FileSystemException("Could not create parent directories");
            }
        }
    }
}