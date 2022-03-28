import java.io.IOException;
import java.net.ServerSocket;

public class SessionManager extends Thread {
    private final int portNumber;
    private final String rootDirectory;

    public SessionManager(int portNumber, String rootDirectory) throws IOException {
        this.portNumber = portNumber;
        this.rootDirectory = rootDirectory;
        this.start();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.printf("File Transfer Server started at %s.%n",
                serverSocket.getLocalSocketAddress());
            while (!interrupted()) {
                Session session = new Session(serverSocket.accept(), rootDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}