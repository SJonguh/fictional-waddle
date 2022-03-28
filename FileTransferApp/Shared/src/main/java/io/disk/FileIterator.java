package io.disk;

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
        if (!hasNext()) return null;

        File file = files[i];
        i++;
        return !file.exists() || file.lastModified() > lastModifiedDate
            ? file.getPath().substring(absolutePathLength)
            : next();
    }

    @Override
    public boolean hasNext() {
        return files != null && i < files.length;
    }
}