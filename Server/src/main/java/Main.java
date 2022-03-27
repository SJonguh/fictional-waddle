import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Main <port number> <root directory>");
        }

        int portNumber = Integer.parseInt(args[0]);
        var sessionManager = new SessionManager(portNumber, args[1]);
    }
}