import io.FileWriteHandler;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Main <host name> <port number> <root directory>");
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String rootDirectory = args[2];

        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            var inputStream = socket.getInputStream();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream))
        ) {
            File file = new File(rootDirectory, "video2.mp4");
            out.println(String.format("%s /%s %s", "PULL", file.getName(), "ABSP/1.0"));
            out.println(String.format("last-modified:%s", "2022-03-21"));
            out.println(String.format("content-length:%d", file.length()));
            out.println();
            out.println();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.isEmpty()) {
                    break;
                }
                System.out.println(String.format("Server: %s", inputLine));
            }

            System.out.println("BYTESTREAM!!");
            FileWriteHandler fileWriteHandler = new FileWriteHandler(file);
            fileWriteHandler.write(inputStream, 5_110_374_380L, "320e94f9878408d757092673939b054a", "MD5");

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
