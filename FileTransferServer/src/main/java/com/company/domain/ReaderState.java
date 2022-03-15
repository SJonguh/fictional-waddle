package com.company.domain;

public enum ReaderState {
    WAITING,
    READING_HEADERS,
    READING_BODY,
    DONE,
}
