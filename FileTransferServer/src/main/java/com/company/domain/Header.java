package com.company.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;

@Getter
@Setter
public class Header extends AbstractMap.SimpleEntry<String, String> {
    public Header(String key, String value) {
        super(key, value);
    }
}
