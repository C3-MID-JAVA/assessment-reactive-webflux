package com.reactivo.banco.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Errores de validación detectados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> handleValidationException(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Errores de validación");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });

        response.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BindException.class)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Errores de validación de campos detectados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> handleBindException(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Errores de validación");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        response.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            )
    })
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(ResourceNotFoundException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "alerta");
        respuesta.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }
}
