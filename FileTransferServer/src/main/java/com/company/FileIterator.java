package com.company;

import java.io.File;
import java.util.Iterator;

public class FileIterator implements Iterator<String> {
    private int i = 0;
    private final File[] files;
    private final int absolutePathLength;
    private final long lastModifiedDate;

    public FileIterator(File[] files, String rootDirectory, long lastModifiedDate) {
        this.files = files;
        this.absolutePathLength = rootDirectory.length();
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String next() {
        return files[i].lastModified() > lastModifiedDate
                ? files[i++].getPath().substring(absolutePathLength)
                : hasNext()
                        ? next()
                        : null;
    }

    @Override
    public boolean hasNext() {
        return files.length > i + 2;
    }
}
