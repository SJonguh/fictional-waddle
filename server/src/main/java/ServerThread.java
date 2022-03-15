import utilities.FileHandler;

import java.io.BufferedReader;
import java.io.File;
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
            socket;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            if (in.ready()) {
                File file = new File("/files/output/video3.mp4");
                FileHandler fileHandler = new FileHandler(file);
                fileHandler.writeFile(socket.getInputStream(), 5_198_882_692L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}