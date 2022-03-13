import java.io.*;

public class FileHandler {

    private static final int BUFFER_SIZE = 8192;
    private File file;

    /**
     * Create a {@code FileHandler} with an internal {@code File} backing field
     * based on the specified {@code pathname}.
     *
     * @param pathname A pathname string
     * @throws NullPointerException If the {@code pathname} argument is {@code null}
     * @author Stephan de Jongh
     */
    public FileHandler(String pathname) {
        file = new File(pathname);
    }

    /**
     * Reads the {@code File} the {@code FileHandler} is pointing towards,
     * into the specified {@code OutputStream}.
     *
     * @param outputStream The {@code OutputStream} to which the file is read
     * @return {@code boolean} indicating whether the file was read.
     * @author Stephan de Jongh
     */
    public boolean read(OutputStream outputStream) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            boolean result = process(fileInputStream, outputStream, file.length());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Writes bytes to the {@code File} the {@code FileHandler}
     * is pointing towards, from the given {@code InputStream}.
     *
     * @param inputStream The {@code InputStream} from which the content is read.
     * @param fileSize    The amount of bytes needed to be read from the {@code InputStream}.
     * @return {@code boolean} indicating whether the file was written successfully.
     * @author Stephan de Jongh
     */
    public boolean write(InputStream inputStream, long fileSize) {
        if (!createDirectories()) {
            return false;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            boolean result = process(inputStream, fileOutputStream, fileSize);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Writes the {@code InputStream} specified to the {@code OutputStream}
     * using an internal {@code byte[]} buffer to handle larger files.
     *
     * @param inputStream  The {@code InputStream} from which the content is read.
     * @param outputStream The {@code OutputStream} to which the content is written.
     * @param fileSize     The amount of bytes needed to be read from the {@code InputStream}
     * @return Amount of bytes written to the {@code OutputStream} as a {@code long}.
     * @author Stephan de Jongh
     */
    private boolean process(InputStream inputStream, OutputStream outputStream, long fileSize) {
        long bytesWritten = 0;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];

            while (bytesWritten < fileSize) {
                int bytesInBuffer = (int) Math.min(fileSize - bytesWritten, BUFFER_SIZE);
                inputStream.read(buffer, 0, bytesInBuffer);
                outputStream.write(buffer, 0, bytesInBuffer);
                bytesWritten += bytesInBuffer;
            }

            System.out.print("BYTES WRITTEN " + bytesWritten);
            System.out.println(" OF TOTAL " + fileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesWritten == fileSize;
    }

    /**
     * Returns the size of the file denoted by this abstract pathname.
     * The return value is unspecified if this pathname denotes a directory.
     *
     * @return The size, in bytes, of the file denoted by this abstract
     * pathname as a {@code long} , or 0L if the file does not exist.
     * @author Stephan de Jongh
     */
    public long fileSize() {
        return file.length();
    }

    /**
     * Converts this abstract pathname into a pathname string.
     *
     * @return The {@code String} form of this abstract pathname.
     * @author Stephan de Jongh
     */
    public String filePath() {
        return file.getPath();
    }

    /**
     * Creates the directory named by this abstract pathname, including any
     * necessary but nonexistent parent directories. Note that if this
     * operation fails it may have succeeded in creating some parent directories.
     *
     * @return {@code boolean} indicating if the directory already exists or was
     * created, along with all the necessary parent directories; false otherwise.
     * @author Stephan de Jongh
     */
    private boolean createDirectories() {
        if (!file.exists()) {
            File directory = new File(file.getParent());
            return directory.mkdirs();
        }
        return true;
    }
}