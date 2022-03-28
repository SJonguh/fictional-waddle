import domain.Headers;
import domain.request.Method;
import domain.request.Request;
import domain.response.Response;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

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
                ResponseProcessor responseProcessor = new ResponseProcessor(socket.getInputStream(), rootDirectory);
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
            Response response = responseProcessor.process(FETCH);
            if(response != null){
                while(response.getBody().hasNext()) {
                    out.process(new Request()
                            .withMethod(new Method()
                                    .withRequestType(PULL)
                                    .withPath((String) response.getBody().next())
                                    .withVersion("ABSP/1"))
                            .withHeaders(new Headers()
                                    .set("last-modified", lastModified)
                                    .set("keep-alive", String.valueOf(true))));
                }
            }




        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
