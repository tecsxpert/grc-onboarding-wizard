package com.internship.tool.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponse() {
        ErrorResponse error = new ErrorResponse(400, "BAD_REQUEST", "Invalid input");

        assertEquals(400, error.getStatus());
        assertEquals("BAD_REQUEST", error.getError());
        assertEquals("Invalid input", error.getMessage());
    }
}