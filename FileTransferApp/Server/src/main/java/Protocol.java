import domain.Headers;
import domain.Validator;
import domain.request.Request;
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
    private final SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy hh:mm");

    public Protocol(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Response process(Request request, InputStream inputStream) throws NoSuchAlgorithmException, IOException, ParseException {
        if(new Validator().validateRequest(request)){
            return new Response(ResponseStatus.B00);
        }

        Headers headers = request.getHeaders();

        // TODO: Check protocol version for accepted versions

        boolean keepAlive = Boolean.parseBoolean(
            headers.getOrDefault("keep-alive", "false"));

        switch (request.getMethod().getRequestType()) {
            case FETCH -> {
                return new Response<>(
                    keepAlive ? ResponseStatus.A00 : ResponseStatus.A01,
                    new FileIterator(
                            new File(rootDirectory, request.getMethod().getPath()).listFiles(),
                            rootDirectory,
                            format.parse(headers.get("last-modified")).getTime()));
            }
            case PUSH -> {
                String checksum = headers.get("checksum");
                String contentLength = headers.get("content-length");
                // TODO: Implement file write handler
                File file = new File(rootDirectory, request.getMethod().getPath());
                FileWriteHandler fileWriteHandler = new FileWriteHandler(file);
                fileWriteHandler.write(inputStream, Long.parseLong(contentLength), checksum, "MD5");
                return new Response<>(keepAlive ? ResponseStatus.A00 : ResponseStatus.A01);
            }
            case PULL -> {
                File file = new File(rootDirectory, request.getMethod().getPath());
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