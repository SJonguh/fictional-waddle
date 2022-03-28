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

    public Response process(RequestType requestType) throws IOException, NoSuchAlgorithmException, ParseException {
        long startTime = System.currentTimeMillis()/1000;
        while(inputStream.available() == 0){
            if((System.currentTimeMillis()/1000) - startTime > 120) {
                return null;
            }
        }

        ProcessState processState = ProcessState.PROCESSING_METHOD;
        Response response = new Response();


        String inputLine;
        while (processState != PROCESSING_BODY && (inputLine = bufferedReader.readLine()) != null) {
            switch (processState) {
                case PROCESSING_METHOD -> {
                    String[] input = inputLine.split(" ", 3);
                    lineValidator.throwExceptionIfInvalidLine(input, 3, "Invalid Method Line");
                    response.setResponseStatus(ResponseStatus.valueOf(input[0]));
                    processState = PROCESSING_HEADERS;
                }
                case PROCESSING_HEADERS -> {
                    if (inputLine.isEmpty()) {
                        processState = PROCESSING_BODY;
                        break;
                    }
                    String[] header = inputLine.split(":");
                    lineValidator.throwExceptionIfInvalidLine(header, 2, "Invalid Header Line");
                    response.setHeaders(new Headers());
                    response.getHeaders().put(header[0], header[1]);
                }
            }
        }
        if(requestType == RequestType.FETCH) {
            File[] files = new File[Integer.parseInt(response.getHeaders().get("content-length"))];
            int i = 0;
            while ((inputLine = bufferedReader.readLine()) != null) {
                if (processState == PROCESSING_BODY && !inputLine.equals("")) {
                    files[i++] = new File(rootDirectory, inputLine);
                }
            }
            response.withBody(new FileIterator(
                    files,
                    rootDirectory,
                    0
            ));
        }
        return response;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
