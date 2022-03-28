package domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Method {
    private RequestType requestType;
    private String path;
    private String version;

    public Method withRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public Method withPath(String path) {
        this.path = path;
        return this;
    }

    public Method withVersion(String version) {
        this.version = version;
        return this;
    }
}
