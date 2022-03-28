package domain;

import java.util.HashMap;

public class Headers extends HashMap<String, String> {

    public Headers set(String key, String value) {
        super.put(key, value);
        return this;
    }
}