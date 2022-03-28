import domain.Headers;
import domain.Validator;
import domain.request.Method;
import domain.request.Request;

import java.io.*;
import java.net.Socket;

import static domain.request.RequestType.FETCH;
import static domain.request.RequestType.PULL;

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
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                RequestWriter out = new RequestWriter(outputStream);
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Which path should be synced");
            String path = stdIn.readLine();
            System.out.println("Which is the last sync date");
            String lastModified = stdIn.readLine();

            out.process(new Request()
                    .withMethod(new Method()
                            .withRequestType(FETCH)
                            .withPath(path)
                            .withVersion("ABSP/1"))
                    .withHeaders(new Headers()
                            .set("last-modified", lastModified)
                            .set("keep-alive", String.valueOf(true))));

            out.process(new Request()
                    .withMethod(new Method()
                            .withRequestType(PULL)
                            .withPath(path)
                            .withVersion("ABSP/1"))
                    .withHeaders(new Headers()
                            .set("last-modified", lastModified)
                            .set("keep-alive", String.valueOf(true))));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
