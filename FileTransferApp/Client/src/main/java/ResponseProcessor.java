import domain.Headers;
import domain.ProcessState;
import domain.request.RequestType;
import domain.response.Response;
import domain.response.ResponseStatus;
import io.disk.FileIterator;
import io.web.LineValidator;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import static domain.ProcessState.PROCESSING_BODY;
import static domain.ProcessState.PROCESSING_HEADERS;

public class ResponseProcessor implements Closeable {
    private final InputStream inputStream;
    private final BufferedReader bufferedReader;
    private final LineValidator lineValidator;
    private final String rootDirectory;

    public ResponseProcessor(InputStream inputStream, String rootDirectory) {
        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        this.lineValidator = new LineValidator();
        this.rootDirectory = rootDirectory;
    }

    public Response process(RequestType requestType) throws IOException {
        long startTime = System.currentTimeMillis()/1000;
        while(inputStream.available() == 0){
            if((System.currentTimeMillis()/1000) - startTime > 120) {
                return null;
            }
        }

        ProcessState processState = ProcessState.PROCESSING_METHOD;
        Response response = new Response();

        File[] files = null;
        int i = 0;
        String inputLine;
        while ((processState != PROCESSING_BODY ||
                requestType == RequestType.FETCH) &&
                (inputLine = bufferedReader.readLine()) != null) {
            switch (processState) {
                case PROCESSING_METHOD -> {
                    String[] input = inputLine.split(" ", 3);
                    lineValidator.throwExceptionIfInvalidLine(input, 3, "Invalid Method Line");
                    response.setResponseStatus(ResponseStatus.valueOf(input[0]));
                    response.setHeaders(new Headers());
                    processState = PROCESSING_HEADERS;
                }
                case PROCESSING_HEADERS -> {
                    if (inputLine.isEmpty()) {
                        processState = PROCESSING_BODY;
                        break;
                    }
                    String[] header = inputLine.split(":");
                    lineValidator.throwExceptionIfInvalidLine(header, 2, "Invalid Header Line");
                    response.getHeaders().set(header[0], header[1]);
                }
                case PROCESSING_BODY -> {
                    if(files == null) {
                        files = new File[Integer.parseInt(response.getHeaders().get("content-length"))];
                        response.withBody(new FileIterator(
                                files,
                                rootDirectory,
                                0
                        ));
                    }
                    if(files.length == i) {
                        return response;
                    }
                    files[i++] = new File(rootDirectory, inputLine);

                }
            }
        }
        return response;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
