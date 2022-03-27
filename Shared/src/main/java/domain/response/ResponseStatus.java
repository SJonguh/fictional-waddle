package domain.response;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    A00(ResponseType.A, "Accepted"),
    A01(ResponseType.A, "Accepted and Closed"),
    B00(ResponseType.B, "Bad Request"),
    B01(ResponseType.B, "Checksum Error"),
    B02(ResponseType.B, "Filename Error"),
    B03(ResponseType.B, "Authorisation Refused"),
    B11(ResponseType.B, "Invalid Date error"),
    B12(ResponseType.B, "Invalid Url"),
    C00(ResponseType.C, "Internal Server Error"),
    C01(ResponseType.C, "File has been modified"),
    C02(ResponseType.C, "No Storage Available"),
    C03(ResponseType.C, "Connection Timeout");

    private final ResponseType responseType;
    private final String message;

    ResponseStatus(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.name() + " " + responseType + " " + message;
    }
}