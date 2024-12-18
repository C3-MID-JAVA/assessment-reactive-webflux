package com.bankmanagement.bankmanagement.controller;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.exception.ErrorResponse;
import com.bankmanagement.bankmanagement.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions", description = "Operations related to transactions")
@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Create a new transaction",
            description = "This endpoint allows you to create a new transaction. It calculates fees and updates the account balance based on the transaction type.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transaction successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, invalid transaction data (e.g., invalid amount, missing account number)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Account not found, the provided account number does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Insufficient balance for this transaction",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionResponseDTO transactionResponseDTO = transactionService.create(transactionRequestDTO);
        return new ResponseEntity<>(transactionResponseDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all transactions for an account",
            description = "This endpoint retrieves all transactions associated with a specific account number. If the account does not exist, it returns a 404 Not Found error.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transactions for the given account number",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Account not found, the provided account number does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{accountNumber}/account")
    public ResponseEntity<List<TransactionResponseDTO>> getAllByAccountNumber(
            @Parameter(description = "The account number to retrieve transactions for", required = true)
            @PathVariable String accountNumber
    ){
        List<TransactionResponseDTO> transactionResponseDTOS = transactionService.getAllByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactionResponseDTOS);
    }
}
