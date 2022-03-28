package domain.request;

import domain.Headers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private Method method;
    private Headers headers;

    public Request() {
        this.headers = new Headers();
    }

    public Request withMethod(Method method) {
        this.method = method;
        return this;
    }

    public Request withHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }
}