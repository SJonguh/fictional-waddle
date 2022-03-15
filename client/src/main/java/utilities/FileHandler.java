package utilities;

import java.io.*;
import java.nio.file.FileSystemException;

public class FileHandler {
    private static final int BUFFER_SIZE = 8192;
    private final File file;

    public FileHandler(File file) {
        this.file = file;
    }

    /**
     * Reads the {@code File} the {@code FileHandler} is pointing towards,
     * into the specified {@code OutputStream}.
     *
     * @param out The {@code OutputStream} to which the file is read.
     * @author Stephan de Jongh
     */
    public void readFile(OutputStream out) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            process(in, out, file.length());
        }
    }

    /**
     * Writes bytes to the {@code File} the {@code FileHandler}
     * is pointing towards, from the given {@code InputStream}.
     *
     * @param in    The {@code InputStream} from which the content is read.
     * @param bytes The amount of bytes needed to be read from the {@code InputStream}.
     * @author Stephan de Jongh
     */
    public void writeFile(InputStream in, long bytes) throws IOException {
        if (file.isDirectory()) {
            throw new FileNotFoundException("File is a directory");
        }
        if (!file.exists()) {
            File parent = new File(file.getParent());
            if (!parent.mkdirs() && !parent.isDirectory()) {
                throw new FileSystemException("Could not create parent directories");
            }
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            process(in, out, bytes);
        }
    }

    /**
     * Writes the {@code InputStream} specified to the {@code OutputStream}
     * using an internal {@code byte[]} buffer to handle larger files.
     *
     * @param in    The {@code InputStream} from which the content is read.
     * @param out   The {@code OutputStream} to which the content is written.
     * @param bytes The amount of bytes needed to be read from the {@code InputStream}
     * @author Stephan de Jongh
     */
    private void process(InputStream in, OutputStream out, long bytes) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        ProgressBar pb = new ProgressBar(20);

        for (long i = 0; i < bytes; i = i + BUFFER_SIZE) {
            int amount = (int) Math.min(bytes - i, BUFFER_SIZE);
            if (in == null) {
                throw new IOException("Stream closed");
            }
            int read = in.read(buffer, 0, amount);
            out.write(buffer, 0, amount);

            long transferred = i + read;
            pb.update(transferred, bytes);
        }
    }
}