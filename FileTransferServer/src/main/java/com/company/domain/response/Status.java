package com.company.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.company.domain.response.StatusCategory.*;

@AllArgsConstructor
@Getter
public enum Status {
    A00 (A, 0, "Accepted"),
    A01 (A, 1, "Accepted and Closed"),
    B00 (B, 0, "Bad Request"),
    B01 (B, 1, "Checksum Error"),
    B02 (B, 2, "Filename Error"),
    B03 (B, 3, "Authorisation Refused"),
    B11 (B, 11, "Invalid Date error"),
    B12 (B, 12, "Invalid Url"),
    C00 (C, 0, "Internal Server Error"),
    C01 (C, 1, "File has been modified"),
    C02 (C, 2, "No Storage Available"),
    C03 (C, 3, "Connection Timeout");

    private final StatusCategory statusCategory;
    private final Integer code;
    private final String message;
}
