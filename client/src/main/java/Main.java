import utilities.FileHandler;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        // Check whether a host name and port number are provided.
        if (args.length != 2) {
            System.err.println("Usage: java Main <host name> <port number>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            File file = new File("/files/input/video3.mp4");
            FileHandler fileHandler = new FileHandler(file);
            fileHandler.readFile(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
        }
    }
}