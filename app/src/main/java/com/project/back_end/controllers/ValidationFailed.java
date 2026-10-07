package com.project.back_end.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice //makes it a global exception handler for REST controllers. can return the response directly as JSON (or any other format) in case of errors.
public class ValidationFailed {

    @ExceptionHandler(MethodArgumentNotValidException.class)//ExceptionHandler specifies that this method will handle exceptions of type MethodArgumentNotValidException. This exception is thrown when a validation error occurs on the request body (such as when data in a @RequestBody doesn't match the required constraints).
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {//this method is called when MethodArgumentNotValidException is thrown. Th object (ex) provides access to the binding result of the validation errors, which includes the field errors (for example, which fields failed validation).
        Map<String, String> errors = new HashMap<>();
        
        // Iterate through all the validation errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = error.getDefaultMessage();
            errors.put("message", "" + errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}