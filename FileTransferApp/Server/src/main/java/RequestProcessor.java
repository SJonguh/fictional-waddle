import domain.ProcessState;
import domain.request.Method;
import domain.request.Request;
import domain.request.RequestType;
import domain.response.Response;
import domain.response.ResponseStatus;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class RequestProcessor implements Closeable {
    private final InputStream inputStream;
    private final BufferedReader bufferedReader;
    private final Protocol protocol;

    public RequestProcessor(InputStream inputStream, String rootDirectory) {
        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(
            new InputStreamReader(inputStream));
        this.protocol = new Protocol(rootDirectory);
    }

    public Response process() throws IOException, NoSuchAlgorithmException, ParseException {
        ProcessState processState = ProcessState.PROCESSING_METHOD;
        Request request = new Request();

        String inputLine;
        while (processState != ProcessState.PROCESSING_BODY && (inputLine = bufferedReader.readLine()) != null) {
            switch (processState) {
                case PROCESSING_METHOD -> {
                    String[] input = inputLine.split(" ");
                    if (input.length != 3 || hasEmptyValue(input))
                        return new Response(ResponseStatus.B00);

                    request.setMethod(new Method()
                            .withRequestType(RequestType.valueOf(input[0]))
                            .withPath(input[1])
                            .withVersion(input[2]));
                    processState = ProcessState.PROCESSING_HEADERS;
                }
                case PROCESSING_HEADERS -> {
                    if (inputLine.isEmpty()) {
                        if (request.getHeaders().isEmpty())
                            return new Response(ResponseStatus.B04);
                        processState = ProcessState.PROCESSING_BODY;
                        break;
                    }
                    String[] header = inputLine.split(":", 2);
                    if (hasEmptyValue(header))
                        return new Response(ResponseStatus.B13);
                    request.getHeaders().put(header[0], header[1]);
                }
            }
        }
        return protocol.process(request, inputStream);
    }

    private boolean hasEmptyValue(String[] array) {
        for (String value : array) {
            if (value.isEmpty() || value.isBlank())
                return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}