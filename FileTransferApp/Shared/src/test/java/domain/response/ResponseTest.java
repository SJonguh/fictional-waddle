package domain.response;

import domain.Headers;
import domain.request.RequestType;
import io.disk.FileIterator;
import io.disk.FileStreamIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {
    private Response response;

    @BeforeEach
    void setUp() {
        response = new Response();
    }

    @Test
    void testWithResponseStatus() {
        // Prepare
        ResponseStatus responseStatus = ResponseStatus.A01;
        // Act
        response.withResponseStatus(responseStatus);
        // Assert
        assertEquals(responseStatus, response.getResponseStatus());
    }

    @Test
    void testWithHeaders() {
        // Prepare
        Headers headers = new Headers();
        // Act
        response.withHeaders(headers);
        // Assert
        assertEquals(headers, response.getHeaders());
    }

    @Test
    void testWithBodyFileIterator() {
        // Prepare
        FileIterator body = new FileIterator(new File[0], "", 0);
        // Act
        response.withBody(body);
        // Assert
        assertEquals(body, response.getBody());
    }

    @Test
    void testWithBodyFileStream() throws IOException {
        // Prepare
        FileStreamIterator body = new FileStreamIterator();
        // Act
        response.withBody(body);
        // Assert
        assertEquals(body, response.getBody());
    }
    @Test
    void testWithRequestType() {
        // Prepare
        RequestType requestType = RequestType.FETCH;
        // Act
        response.withRequestType(requestType);
        // Assert
        assertEquals(requestType, response.getRequestType());
    }
}
