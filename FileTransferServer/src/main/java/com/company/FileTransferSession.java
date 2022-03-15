package com.company;

import com.company.domain.Header;
import com.company.domain.response.FileTransferResponse;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;

public class FileTransferSession extends Thread {
    private final Socket connection;
    private final FileTransferProtocol fileTransferProtocol;
    private final RequestReader requestReader;

    public FileTransferSession(Socket connection, String rootDirectory){
        this.connection = connection;
        this.requestReader = new RequestReader();
        this.fileTransferProtocol = new FileTransferProtocol(rootDirectory);
        start();
    }

    @Override
    public void run() {
        System.out.println("Session started");
        while( !interrupted() ) {
            try (
                    PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))
            ) {
                String inputLine;
                out.println("Initiated TCP Session: waiting");
                while ((inputLine = in.readLine()) != null) {
                    switch (requestReader.readInput(inputLine).getReaderState()) {
                        case READING_BODY -> {
                            FileTransferResponse response = fileTransferProtocol.process(requestReader.getRequest(), connection.getInputStream(), connection.getOutputStream());
                            out.println(response.getStatus().getCode() + response.getStatus().getStatusCategory().getMessage() + response.getStatus().getMessage());
                            for (Header header : response.getHeaders().getList()) {
                                out.println(header.getKey() + ":" + header.getValue());
                            }

                            FileIterator files;
                            String file;
                            if((files = response.getBody()) != null) {
                                while((file = files.next()) != null) {
                                    out.println(file);
                                }
                            }
                        }
                        case DONE -> requestReader.reset();
                    }
                }
            } catch(IOException e) {
                //empty catch
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
