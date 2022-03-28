package io.web;

import domain.Headers;
import domain.request.Method;
import domain.request.Request;
import domain.request.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void testValidateRequestNull() {
        // Assert
        assertFalse(validator.validateRequest(null));
    }

    @Test
    void testValidateRequestGetMethod() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(null)));
    }

    @Test
    void testValidateRequestGetRequest() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(null))));
    }

    @Test
    void testValidateRequestGetPath() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath(null))));
    }

    @Test
    void testValidateRequestGetVersion() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion(null))));
    }

    @Test
    void testValidateRequest() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))));
    }

    @Test
    void testValidateRequestGetHeaders() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(null)));
    }

    @Test
    void testValidateRequestGetHeadersIsEmpty() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers())));
    }

    @Test
    void testValidateRequestGetHeadersInvalidPushContentLength() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.PUSH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1"))));
    }

    @Test
    void testValidateRequestGetHeadersInvalidPushChecksum() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.PUSH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("checksum", "1"))));
    }

    @Test
    void testValidateRequestGetHeadersValidPushContentLengthChecksum() {
        // Assert
        assertTrue(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.PUSH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1")
                        .set("checksum", "1"))));
    }

    @Test
    void testValidateRequestGetHeadersInvalidFetchContentLengthChecksum() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1")
                        .set("checksum", "1"))));
    }

    @Test
    void testValidateRequestGetHeadersInvalidPullContentLengthChecksum() {
        // Assert
        assertFalse(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.PULL)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1")
                        .set("checksum", "1"))));
    }

    @Test
    void testValidateRequestGetHeadersValidFetchContentLengthChecksum() {
        // Assert
        assertTrue(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.FETCH)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1")
                        .set("checksum", "1")
                        .set("last-modified", "1"))));

    }

    @Test
    void testValidateRequestGetHeadersValidPullContentLengthChecksum() {
        // Assert
        assertTrue(validator.validateRequest(new Request()
                .withMethod(new Method()
                        .withRequestType(RequestType.PULL)
                        .withPath("c:/sample/data")
                        .withVersion("Version 1.0"))
                .withHeaders(new Headers()
                        .set("content-length", "1")
                        .set("checksum", "1")
                        .set("last-modified", "1"))));
    }
}
