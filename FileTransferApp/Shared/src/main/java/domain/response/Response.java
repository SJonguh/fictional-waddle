package domain.response;

import domain.Headers;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

@Getter
@Setter
public class Response<T> {
    private ResponseStatus responseStatus;
    private Headers headers;
    private Iterator<T> iterator;

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Response(ResponseStatus responseStatus, Iterator<T> iterator) {
        this.responseStatus = responseStatus;
        this.iterator = iterator;
    }

    public Response(ResponseStatus responseStatus, Headers headers, Iterator<T> iterator) {
        this.responseStatus = responseStatus;
        this.headers = headers;
        this.iterator = iterator;
    }
}