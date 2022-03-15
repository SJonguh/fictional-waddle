package com.company;

import com.company.domain.Header;
import com.company.domain.ReaderState;
import com.company.domain.request.FileTransferRequest;
import com.company.domain.request.Method;
import lombok.Getter;

import java.util.AbstractMap;

import static com.company.domain.ReaderState.*;

@Getter
public class RequestReader {
    private ReaderState readerState = WAITING;
    private FileTransferRequest request = new FileTransferRequest();

    public void reset(){
        readerState = WAITING;
        request = new FileTransferRequest();
    }

    public RequestReader readInput(String input){
        switch (readerState) {
            case WAITING -> {
                request.setMethod(Method.valueOf(input.split(" ")[0]));
                request.setPath(input.split(" ")[1]);
                readerState = READING_HEADERS;
            }
            case READING_HEADERS -> {
                if (input.equals("")) {
                    readerState = READING_BODY;
                    break;
                }
                String[] keyValues = input.split(": ");
                request.getHeaders().getList().add(new Header(keyValues[0], keyValues[1]));
            }
            case READING_BODY -> {
                if (input.equals("")) {
                    readerState = DONE;
                }
            }
        }
        return this;
    }

}
