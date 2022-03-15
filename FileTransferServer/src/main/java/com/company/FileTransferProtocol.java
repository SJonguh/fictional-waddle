package com.company;

import com.company.domain.Headers;
import com.company.domain.request.FileTransferRequest;
import com.company.domain.response.FileTransferResponse;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import static com.company.domain.response.Status.*;

@AllArgsConstructor
public class FileTransferProtocol {

    private String root;

    public FileTransferResponse process(FileTransferRequest fileTransferRequest, InputStream is, OutputStream os) throws ParseException, IOException {
        if(fileTransferRequest == null
                || fileTransferRequest.getPath() == null
                || fileTransferRequest.getMethod() == null) {
            return new FileTransferResponse( B00, null, null);
        }

        String lastModifiedDate = fileTransferRequest.getHeaders().getHeader("last-modified");
        String contentLength = fileTransferRequest.getHeaders().getHeader("content-length");

        if (lastModifiedDate == null || contentLength == null) {
            return new FileTransferResponse( B00, new Headers(), null);
        }

        switch (fileTransferRequest.getMethod()) {
            case FETCH:
                File[] files =  new File(root + fileTransferRequest.getPath()).listFiles();
                return new FileTransferResponse(
                        A00,
                        new Headers(),
                        new FileIterator(files, root, Long.parseLong(lastModifiedDate)));
            case PUSH:
                new FileHandler(new File(root + fileTransferRequest.getPath())).writeFile(is, Long.parseLong(contentLength));
                return new FileTransferResponse(
                        A00,
                        new Headers(),
                        null);
            case PULL:
                new FileHandler(new File(root + fileTransferRequest.getPath())).readFile(os);
                return new FileTransferResponse(
                        A00,
                        new Headers(),
                        null);
            default:
                return null;
        }
    }

}
