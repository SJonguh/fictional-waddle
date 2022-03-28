import domain.Headers;
import domain.response.Response;
import io.FileIterator;
import io.FileStreamIterator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ResponseWriter extends PrintWriter {
    private final OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream, boolean autoFlush) {
        super(outputStream, autoFlush);
        this.outputStream = outputStream;
    }

    public void process(Response response) throws IOException {
        // Return response status
        this.println(response.getResponseStatus());

        // Add headers to response
        Headers headers = response.getHeaders();
        if (headers != null) {
            headers.forEach((key, value) -> {
                this.println(String.format("%s:%s", key, value));
            });
        }
        this.println(); // Empty line between headers and body

        var iterator = response.getIterator();
        // Iterate over a file directory and return all files
        if (iterator instanceof FileIterator) {
            while (iterator.hasNext()) {
                this.println((String) iterator.next());
            }
        }
        // Iterate over a file content and return a byte stream
        if (iterator instanceof FileStreamIterator) {
            while (iterator.hasNext()) {
                outputStream.write((byte[]) iterator.next());
            }
        }
        this.println();
    }
}