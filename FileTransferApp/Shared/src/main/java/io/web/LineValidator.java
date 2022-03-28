package io.web;

public class LineValidator {
    public String[] throwExceptionIfInvalidLine(String[] data, int length, String message) {
        if(isInvalidLine(data, length)){
            throw new RuntimeException(message);
        }
        return data;
    }

    public boolean isInvalidLine(String[] array, int length) {
        if (length != array.length) {
            return true;
        }
        for (String value : array) {
            if (value.isEmpty() || value.isBlank())
                return true;
        }
        return false;
    }
}
