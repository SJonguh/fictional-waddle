package io.web;

import domain.request.Request;
import domain.response.Response;

public class Validator {
    public boolean validateRequest(Request request){
        return !(request == null ||
                request.getMethod() == null ||
                request.getMethod().getRequestType() == null ||
                request.getMethod().getPath() == null ||
                request.getMethod().getVersion() == null ||
                request.getHeaders() == null ||
                request.getHeaders().isEmpty() ||
                !validateRequestTypeSpecificHeaders(request));
    }

    public boolean validateResponse(Response response){
        return !(response == null ||
                response.getResponseStatus() == null ||
                response.getBody() == null ||
                response.getHeaders() == null ||
                response.getHeaders().isEmpty());
    }

    private boolean validateRequestTypeSpecificHeaders(Request request) {
        return switch(request.getMethod().getRequestType()) {
            case PUSH ->
                    request.getHeaders().get("content-length") != null &&
                            request.getHeaders().get("") != null;
            case PULL, FETCH ->
                    request.getHeaders().get("last-modified") != null;
        };
    }

}
