package com.company.domain.response;

import com.company.FileIterator;
import com.company.domain.Headers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.Iterator;

@AllArgsConstructor
@Getter
@Setter
public class FileTransferResponse {
    private Status status;
    private Headers headers;
    private FileIterator body;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(status.getCode())
                .append(status.getStatusCategory().getMessage())
                .append(": ")
                .append(status.getMessage());

        sb.append(headers);

        return sb.append(System.getProperty("line.separator"))
                .append(body)
                .toString();
    }
}
