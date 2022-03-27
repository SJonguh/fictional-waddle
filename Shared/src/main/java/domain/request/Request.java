package domain.request;

import domain.Headers;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

@Getter
@Setter
public class Request {
    private RequestType requestType;
    private String path;
    private String version;
    private Headers headers;
    private Iterator body;

    public Request() {
    }
}