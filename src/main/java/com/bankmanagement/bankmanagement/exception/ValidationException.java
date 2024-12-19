package com.bankmanagement.bankmanagement.exception;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
