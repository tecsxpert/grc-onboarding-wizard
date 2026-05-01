package com.internship.tool.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    // =========================
    // RESOURCE NOT FOUND
    // =========================
    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("User", "id", 1);

        assertTrue(ex.getMessage().contains("User"));
    }

    // =========================
    // DUPLICATE
    // =========================
    @Test
    void testDuplicateResourceException() {
        DuplicateResourceException ex =
                new DuplicateResourceException("Already exists");

        assertEquals("Already exists", ex.getMessage());
    }

    // =========================
    // INVALID INPUT
    // =========================
    @Test
    void testInvalidInputException() {
        InvalidInputException ex =
                new InvalidInputException("Invalid");

        assertEquals("Invalid", ex.getMessage());
    }

    // =========================
    // GLOBAL HANDLER
    // =========================
    @Test
    void handleNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleNotFound(
                        new ResourceNotFoundException("User", "id", 1)
                );

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void handleInvalidInput() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidInput(
                        new InvalidInputException("Invalid")
                );

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void handleDuplicate() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicate(
                        new DuplicateResourceException("Duplicate")
                );

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void handleIllegalArgument() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgument(
                        new IllegalArgumentException("Bad")
                );

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void handleJsonError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleJsonError(null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void handleGeneralException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneral(new Exception("Error"));

        assertEquals(500, response.getStatusCodeValue());
    }
}