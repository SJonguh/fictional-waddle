import domain.response.Response;
import domain.response.ResponseStatus;
import domain.response.ResponseWriter;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class Session extends Thread {
    private final Socket socket;
    private final String rootDirectory;

    public Session(Socket socket, String rootDirectory) {
        this.socket = socket;
        this.rootDirectory = rootDirectory;
        this.start();
    }

    @Override
    public void run() {
        try (
            socket;
            ResponseWriter out = new ResponseWriter(socket.getOutputStream(), true);
            RequestProcessor requestProcessor = new RequestProcessor(
                socket.getInputStream(), rootDirectory)
        ) {
            while (!interrupted()) {
                Response response = requestProcessor.process();
                out.process(response);
                if (response.getResponseStatus() == ResponseStatus.A01)
                    break;
            }
        } catch (IOException | NoSuchAlgorithmException | ParseException e) {
            e.printStackTrace();
        }
    }
}