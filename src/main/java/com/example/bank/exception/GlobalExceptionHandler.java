package com.example.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> err = new HashMap<>();
        err.put("message", "Something went wrong. " + ex.getMessage());
        err.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString()); //500

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> err = new HashMap<>();
        err.put("message", "Argument not valid. " + ex.getMessage());
        err.put("status", HttpStatus.BAD_REQUEST.toString()); //400

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(IllegalArgumentException ex) {
        Map<String, String> err = new HashMap<>();
        err.put("message", "Argument not valid. " + ex.getMessage());
        err.put("status", HttpStatus.BAD_REQUEST.toString()); //400

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> err = new HashMap<>();
        err.put("message", "No such element. " + ex.getMessage());
        err.put("status", HttpStatus.NOT_FOUND.toString()); //404

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, String> err = new HashMap<>();
        err.put("message", "HTTP Request method not allowed. " + ex.getMessage());
        err.put("status", HttpStatus.METHOD_NOT_ALLOWED.toString()); //400

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }
}