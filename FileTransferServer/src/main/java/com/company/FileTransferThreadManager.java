package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferThreadManager extends Thread {
    private final ServerSocket serverSocket;
    private final FileTransferSession[] clientConnections = new FileTransferSession[ 1 ];
    private final String rootDirectory;

    public FileTransferThreadManager(int port, String rootDirectory) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rootDirectory = rootDirectory;
        start();
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                assignConnectionToSession( this.serverSocket.accept() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void assignConnectionToSession( Socket connection ) {
        for ( int i = 0 ; i < 1 ; i++ ) {
            //find an unassigned subserver (waiter)
            if ( this.clientConnections[ i ] == null ) {
                this.clientConnections[ i ] = new FileTransferSession(connection, rootDirectory);
                System.out.println("Created a session");
                break;
            }
        }
    }
}
