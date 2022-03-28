package domain.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTest {
    private Method method;

    @BeforeEach
    void setUp() {
        method = new Method();
    }

    @Test
    void testWithRequestType() {
        // Prepare
        RequestType requestType = RequestType.FETCH;
        // Act
        method.withRequestType(requestType);
        // Assert
        assertEquals(RequestType.FETCH, method.getRequestType());
    }

    @Test
    void testWithPath() {
        // Prepare
        String path = "C:/Sample/Data";
        // Act
        method.withPath(path);
        // Assert
        assertEquals(path, method.getPath());
    }

    @Test
    void testWithVersion() {
        // Prepare
        String version = "Version 1.0";
        // Act
        method.withVersion(version);
        // Assert
        assertEquals(version, method.getVersion());
    }

}
