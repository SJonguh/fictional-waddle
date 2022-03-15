package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("First argument should be port number, and second argument should be the root directory");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        new FileTransferThreadManager(portNumber, args[1]);
    }

}
