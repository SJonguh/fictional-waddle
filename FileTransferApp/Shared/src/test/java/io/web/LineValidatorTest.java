package io.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineValidatorTest {
    private LineValidator lineValidator;

    @BeforeEach
    void setUp() {
        lineValidator = new LineValidator();
    }

    @Test
    void testThrowExceptionIfInvalidLine() {
        // Act
        try {
            lineValidator.throwExceptionIfInvalidLine(new String[0], 1, "Message");

        } catch (RuntimeException e) {
            assertEquals("Message", e.getMessage());
        }

    }

    @Test
    void testThrowExceptionIfInvalidIsEmptyLine() {
        // Act
        try {
            lineValidator.throwExceptionIfInvalidLine(new String[]{""}, 1, "Message");

        } catch (RuntimeException e) {
            assertEquals("Message", e.getMessage());
        }

    }

    @Test
    void testThrowExceptionIfInvalidBlankLine() {
        // Act
        try {
            lineValidator.throwExceptionIfInvalidLine(new String[]{" "}, 1, "Message");

        } catch (RuntimeException e) {
            assertEquals("Message", e.getMessage());
        }

    }

    @Test
    void testThrowExceptionIfValidLine() {
        // Act
        lineValidator.throwExceptionIfInvalidLine(new String[]{"Random String"}, 1, "Message");
    }

}
