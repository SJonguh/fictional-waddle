package com.company.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Headers {
    private final List<Header> list = new ArrayList<>();

    public String getHeader(String headerName){
        while (list.iterator().hasNext()) {
            Header header = list.iterator().next();
            if(header.getKey().equals(headerName)) {
                return header.getValue();
            }
        }
        return null;
    }
}
