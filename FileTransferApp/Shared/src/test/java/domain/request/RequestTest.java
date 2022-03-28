package domain.request;

import domain.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
    private Request request;

    @BeforeEach
    void setUp() {
        request = new Request();
    }

    @Test
    void testWithMethod() {
        // Prepare
        Method method = new Method();
        // Act
        request.withMethod(method);
        // Assert
        assertEquals(method, request.getMethod());
    }

    @Test
    void testWithHeaders() {
        // Prepare
        Headers headers = new Headers();
        // Act
        request.withHeaders(headers);
        // Assert
        assertEquals(headers, request.getHeaders());
    }
}
