package com.bankmanagement.bankmanagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(description = "Standard error response format")
@Data
public class ErrorResponse {

    @Schema(description = "Http Status", example = "BAD_REQUEST")
    private HttpStatus status;

    @Schema(description = "Error message providing more details", example = "The provided account number does not exist in the system.")
    private String error;

    private LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
