package domain.response;

public enum ResponseType {
    A("Request was successfully processed:"),
    B("A client error has occurred:"),
    C("A server error has occurred:");

    private final String message;

    ResponseType(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}