package domain;

public enum ProcessState {
    WAITING,
    PROCESSING_HEADERS,
    PROCESSING_BODY,
    DONE
}