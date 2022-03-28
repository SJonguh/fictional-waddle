import domain.Headers;
import domain.request.RequestType;
import io.web.Validator;
import domain.request.Request;
import domain.response.Response;
import domain.response.ResponseStatus;
import io.disk.FileIterator;
import io.disk.FileReadHandler;
import io.disk.FileWriteHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class Protocol {
    private final String rootDirectory;
    private final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy hh:mm");

    public Protocol(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Response process(Request request, InputStream inputStream) throws NoSuchAlgorithmException, IOException, ParseException {
        if(new Validator().validateRequest(request)){
            return new Response().withResponseStatus(ResponseStatus.B00);
        }

        Headers headers = request.getHeaders();

        // TODO: Check protocol version for accepted versions

        boolean keepAlive = Boolean.parseBoolean(
            headers.getOrDefault("keep-alive", "false"));

        switch (request.getMethod().getRequestType()) {
            case FETCH -> {
                File[] files = new File(rootDirectory, request.getMethod().getPath()).listFiles();
                return new Response()
                        .withResponseStatus(keepAlive
                                ? ResponseStatus.A00
                                : ResponseStatus.A01)
                        .withHeaders(new Headers()
                                .set("content-length", String.valueOf(files.length)))
                        .withBody(new FileIterator(
                                files,
                                rootDirectory,
                                format.parse(headers.get("last-modified")).getTime()))
                        .withRequestType(RequestType.FETCH);
            }
            case PUSH -> {
                String checksum = headers.get("checksum");
                String contentLength = headers.get("content-length");
                // TODO: Implement file write handler
                File file = new File(rootDirectory, request.getMethod().getPath());
                FileWriteHandler fileWriteHandler = new FileWriteHandler(file);
                fileWriteHandler.write(inputStream, Long.parseLong(contentLength), checksum, "MD5");
                return new Response()
                        .withResponseStatus(keepAlive
                                ? ResponseStatus.A00
                                : ResponseStatus.A01)
                        .withRequestType(RequestType.PUSH);

            }
            case PULL -> {
                File file = new File(rootDirectory, request.getMethod().getPath());
                FileReadHandler fileReadHandler = new FileReadHandler(file);
                return new Response()
                        .withResponseStatus(keepAlive
                                ? ResponseStatus.A00
                                : ResponseStatus.A01)
                        .withHeaders(new Headers()
                                .set("content-length", String.valueOf(file.length()))
                                .set("checksum", fileReadHandler.getChecksum("MD5")))
                        .withBody(fileReadHandler.getIterator())
                        .withRequestType(RequestType.PULL);

            }
            default -> {
                return new Response().withResponseStatus(ResponseStatus.B00);
            }
        }
    }
}