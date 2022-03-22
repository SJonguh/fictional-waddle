import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class FileReadHandler {
    private final File file;
    private final int DEFAULT_BUFFER_SIZE = Short.MAX_VALUE;
    private final String DEFAULT_MESSAGE_DIGEST = "MD5";

    public FileReadHandler(File file) {
        this.file = file;
    }

    public FileContentIterator getIterator() throws IOException {
        return getIterator(DEFAULT_BUFFER_SIZE);
    }

    public FileContentIterator getIterator(int bufferSize) throws IOException {
        return new FileContentIterator(file, bufferSize);
    }

    public InputStream getContentStream() throws IOException {
        return (InputStream) StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(getIterator(), Spliterator.ORDERED), false);
    }

    public String getChecksum() throws NoSuchAlgorithmException, IOException {
        return getChecksum(DEFAULT_MESSAGE_DIGEST);
    }

    public String getChecksum(String digest) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(digest);
        try(FileContentIterator fileContentIterator = getIterator()) {
            while (fileContentIterator.hasNext()) {
                messageDigest.update(fileContentIterator.next());
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : messageDigest.digest()) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        }
    }
}