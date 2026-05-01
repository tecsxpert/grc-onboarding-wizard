package com.internship.tool.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // ✅ ResourceNotFound
    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Not found", response.getBody().getMessage());
    }

    // ✅ Invalid Input
    @Test
    void testHandleInvalidInput() {
        InvalidInputException ex = new InvalidInputException("Bad input");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidInput(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    // ✅ Duplicate
    @Test
    void testHandleDuplicate() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate");

        ResponseEntity<ErrorResponse> response = handler.handleDuplicate(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    // ✅ Validation (mocked)
    @Test
    void testHandleValidation() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    // ✅ JSON error
    @Test
    void testHandleJsonError() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Invalid JSON");

        ResponseEntity<ErrorResponse> response = handler.handleJsonError(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    // ✅ Illegal argument
    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Wrong");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    // ✅ Generic exception
    @Test
    void testHandleGeneral() {
        Exception ex = new Exception("Error");

        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertEquals(500, response.getStatusCode().value());
    }
}