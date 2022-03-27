import domain.Headers;
import domain.ProcessState;
import domain.request.Request;
import domain.request.RequestType;
import domain.response.ResponseStatus;
import domain.response.ResponseWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class Session extends Thread {
    private final Socket socket;
    private final Protocol protocol;
    private ProcessState processState;
    private Request request;

    public Session(Socket socket, String rootDirectory) {
        this.socket = socket;
        this.protocol = new Protocol(rootDirectory);
        this.processState = ProcessState.WAITING;
        this.start();
    }

    @Override
    public void run() {
        // Session started between client and server
        try (
            socket;
            ResponseWriter out = new ResponseWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println(String.format("TCP Session initiated with: %s:%s.", socket.getInetAddress(), socket.getPort()));
            process:
            while (!interrupted()) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) { //while(processState == ProcessState.PROCESSING_BODY || (inputLine = in.readLine()) != null)
                    switch (processState) {
                        case WAITING -> {
                            var input = inputLine.split(" ");
                            request = new Request();
                            request.setRequestType(RequestType.valueOf(input[0]));
                            request.setPath(input[1]);
                            request.setVersion(input[2]);
                            request.setHeaders(new Headers());
                            processState = ProcessState.PROCESSING_HEADERS;
                        }
                        case PROCESSING_HEADERS -> {
                            if (inputLine.isEmpty()) {
                                processState = ProcessState.PROCESSING_BODY;
                            } else {
                                var header = inputLine.split(":");
                                request.getHeaders().put(header[0], header[1]);
                            }
                        }
                        case PROCESSING_BODY -> {
                            var response = protocol.process(request, socket.getInputStream());
                            out.process(response);
                            processState = ProcessState.WAITING;

                            if (response.getResponseStatus() == ResponseStatus.A01) {
                                break process;
                            }
                        }
                    }
                    System.out.println(inputLine); // TODO: Remove after testing
                }
            }
        } catch (IOException | NoSuchAlgorithmException | ParseException e) {
            e.printStackTrace();
        }
    }
}