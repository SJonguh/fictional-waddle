package com.company.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCategory {
    A ("Request was successfully processed"),
    B ("A client error has occured"),
    C ("A server error has occured");

    private final String message;
}
