import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket socket;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input =  new BufferedReader(
                new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine, outputLine;
            while((inputLine = input.readLine())!= null) {

            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}