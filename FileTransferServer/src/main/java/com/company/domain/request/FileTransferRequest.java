package com.company.domain.request;

import com.company.domain.Headers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileTransferRequest {
    private Method method;
    private String path;
    private String protocolVersion;
    private Headers headers;
}
