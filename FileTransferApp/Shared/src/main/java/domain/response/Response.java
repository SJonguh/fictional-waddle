package domain.response;

import domain.Headers;
import domain.request.Request;
import domain.request.RequestType;
import io.disk.FileIterator;
import io.disk.FileStreamIterator;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Iterator;

@Getter
@Setter
public class Response {
    private ResponseStatus responseStatus;
    private Headers headers;
    private Iterator body;
    private RequestType requestType;

    public Response withResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    public Response withHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }

    public Response withBody(FileIterator body) {
        this.body = body;
        return this;
    }

    public Response withBody(FileStreamIterator body) {
        this.body = body;
        return this;
    }

    public Response withRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }
}