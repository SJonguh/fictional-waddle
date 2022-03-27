package domain.request;

import domain.Headers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private RequestType requestType;
    private String path;
    private String version;
    private Headers headers;

    public Request() {
        this.headers = new Headers();
    }
}