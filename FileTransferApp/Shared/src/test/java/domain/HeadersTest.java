package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeadersTest {
    private Headers headers;

    @BeforeEach
    void setUp() {
        headers = new Headers();
    }

    @Test
    void setTest() {
        // Prepare
        String key = "";
        String value = "";
        // Act
        headers.set(key, value);
        // Assert
        assertEquals(value, headers.get(key));
    }
}
