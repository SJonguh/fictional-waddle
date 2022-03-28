import domain.Headers;
import domain.request.Method;
import domain.request.Request;
import domain.response.Response;
import domain.response.ResponseStatus;
import io.disk.FileWriteHandler;

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

            Response response = null;

            out.process(new Request()
                    .withMethod(new Method()
                            .withRequestType(FETCH)
                            .withPath(path)
                            .withVersion("ABSP/1"))
                    .withHeaders(new Headers()
                            .set("last-modified", lastModified)
                            .set("keep-alive", String.valueOf(true))));
            response = responseProcessor.process(FETCH);
            if(response != null && response.getResponseStatus() == ResponseStatus.A00){
                String filePath = null;
                while(response.getBody().hasNext()) {
                    filePath = (String) response.getBody().next();
                    out.process(new Request()
                            .withMethod(new Method()
                                    .withRequestType(PULL)
                                    .withPath(filePath)
                                    .withVersion("ABSP/1"))
                            .withHeaders(new Headers()
                                    .set("last-modified", lastModified)
                                    .set("keep-alive", String.valueOf(true))));
                    response = responseProcessor.process(PULL);

                    String checksum = response.getHeaders().get("checksum");
                    String contentLength = response.getHeaders().get("content-length");
                    File file = new File(rootDirectory, filePath);
                    FileWriteHandler fileWriteHandler = new FileWriteHandler(file);
                    fileWriteHandler.write(inputStream, Long.parseLong(contentLength), checksum, "MD5");
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
