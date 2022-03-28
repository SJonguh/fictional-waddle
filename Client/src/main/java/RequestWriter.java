import io.web.Validator;
import domain.request.Request;
import io.disk.FileStreamIterator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class RequestWriter extends PrintWriter {
    private final OutputStream outputStream;

    public RequestWriter(OutputStream outputStream) {
        super(outputStream, true);
        this.outputStream = outputStream;
    }

    public void process(Request request) throws IOException {
        Validator validator = new Validator();
        if(validator.validateRequest(request)){
            throw new RuntimeException("Request validation failed");
        }

        println(request.getMethod().getRequestType() + " " + request.getMethod().getPath() + " " + request.getMethod().getVersion());

        request.getHeaders().forEach((key, value) -> {
            println(String.format("%s:%s", key, value));
        });

        println("");
    }

    public void process(Request request, FileStreamIterator iterator) throws IOException {
        process(request);

        byte[] bytes;
        while((bytes = iterator.next()) != null) {
            outputStream.write(bytes);
        }

        println("");
    }
}
