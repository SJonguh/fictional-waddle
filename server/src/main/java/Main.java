import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        // Check whether a port number is provided.
        if (args.length != 1) {
            System.err.println("Usage: java Main <port number>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Exception when trying to listen on port " + port + " or listening for a connection");
            System.exit(1);
        }
    }
}
