import domain.Headers;
import domain.request.Request;
import domain.request.RequestType;
import domain.response.Response;
import domain.response.ResponseStatus;
import io.FileIterator;
import io.FileReadHandler;
import io.FileWriteHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Protocol {
    private final String rootDirectory;

    public Protocol(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Response process(Request request, InputStream inputStream) throws NoSuchAlgorithmException, IOException, ParseException {
        if (request == null || request.getPath() == null || request.getRequestType() == null) {
            return new Response<>(ResponseStatus.B00);
        }
        var headers = request.getHeaders();

        // TODO: Check protocol version for accepted versions

        long lastModified = new SimpleDateFormat("dd-MM-yyyy")
            .parse(headers.get("last-modified"))
            .getTime();
        String contentLength = headers.get("content-length");
        boolean keepAlive = Boolean.parseBoolean(
            headers.getOrDefault("keep-alive", "false"));

        if (/*lastModified == null ||*/ (request.getRequestType() == RequestType.PUSH && contentLength == null)) {
            return new Response<>(ResponseStatus.B00);
        }

        switch (request.getRequestType()) {
            case FETCH -> {
                File file = new File(rootDirectory, request.getPath());
                File[] files = file.listFiles();
                return new Response<>(
                    keepAlive ? ResponseStatus.A00 : ResponseStatus.A01,
                    new FileIterator(files, rootDirectory, lastModified));
            }
            case PUSH -> {
                // TODO: Implement file write handler
                File file = new File(rootDirectory, request.getPath());
                FileWriteHandler fileWriteHandler = new FileWriteHandler(file);
                return new Response<>(keepAlive ? ResponseStatus.A00 : ResponseStatus.A01);
            }
            case PULL -> {
                File file = new File(rootDirectory, request.getPath());
                FileReadHandler fileReadHandler = new FileReadHandler(file);
                return new Response<>(
                    keepAlive ? ResponseStatus.A00 : ResponseStatus.A01,
                    new Headers()
                        .set("content-length", String.valueOf(file.length()))
                        .set("checksum", fileReadHandler.getChecksum("MD5")),
                    fileReadHandler.getIterator());
            }
            default -> {
                return new Response<>(ResponseStatus.B00, null, null);
            }
        }
    }
}