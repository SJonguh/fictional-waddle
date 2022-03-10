import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        try(
            Socket socket = new Socket(host, port);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in));
            String response, request;

            while((response = input.readLine()) != null) {
                request = stdInput.readLine();
                if (request != null) {

                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            System.exit(1);
        }
    }
}