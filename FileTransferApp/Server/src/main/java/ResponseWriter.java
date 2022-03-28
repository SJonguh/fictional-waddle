import domain.Headers;
import domain.request.Request;
import domain.request.RequestType;
import domain.response.Response;
import io.disk.FileIterator;
import io.disk.FileStreamIterator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import static domain.request.RequestType.FETCH;
import static domain.request.RequestType.PULL;

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
                println(String.format("%s:%s", key, value));
            });
        }

        println(); // Empty line between headers and body

        // Iterate over a file directory and return all files
        while (response.getBody() != null && response.getBody().hasNext()) {
            if (response.getRequestType() == FETCH) {
                println((String) response.getBody().next());
            } else if(response.getRequestType() == PULL){
                outputStream.write((byte[]) response.getBody().next());
            }
        }

        println(); // Empty line at the end of the request
    }
}