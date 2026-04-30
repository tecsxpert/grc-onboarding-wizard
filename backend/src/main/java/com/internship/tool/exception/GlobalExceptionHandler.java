package com.internship.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 404 - Resource Not Found
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(404, "NOT_FOUND", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // =========================
    // 400 - Invalid Input (Custom)
    // =========================
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // =========================
    // 400 - Duplicate Resource
    // =========================
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(400, "BAD_REQUEST", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // =========================
    // 400 - Validation Errors (@Valid)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .orElse("Validation error");

        return new ResponseEntity<>(
                new ErrorResponse(400, "BAD_REQUEST", message),
                HttpStatus.BAD_REQUEST
        );
    }

    // =========================
    // 500 - Generic Exception
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(500, "INTERNAL_SERVER_ERROR", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}