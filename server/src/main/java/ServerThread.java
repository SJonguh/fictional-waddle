import java.io.*;
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
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
        ) {
            if (in.available() > 0) {
                var firstLine = in.readUTF();
                System.out.println(firstLine);

                FileHandler fh = new FileHandler("output/video.mp4");
                fh.write(in, 47_224_582L);

                var lastLine = in.readUTF();
                System.out.println(lastLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}