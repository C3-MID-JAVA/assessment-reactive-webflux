package com.sofka.bank.controller;

import com.sofka.bank.dto.TransactionDTO;
import com.sofka.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get balance by account id", description = "Retrieve current balance by account id.")
    @ApiResponse(responseCode = "200", description = "Account balance retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/balance/{accountId}")
    public Mono<ResponseEntity<Double>> getGlobalBalance(
            @Parameter(description = "Id of account to retrieve balance")
            @PathVariable String accountId) {
        logger.info("Fetching global balance for account ID: {}", accountId);

        return transactionService.getGlobalBalance(accountId)
                .map(balance -> {
                    logger.info("Global balance for account ID {}: {}", accountId, balance);
                    return ResponseEntity.ok(balance);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @Operation(summary = "Create a new transaction", description = "Creates a new transaction for the account with " +
            "the provided id.")
    @ApiResponse(responseCode = "200", description = "Transaction registered successfully")
    @ApiResponse(responseCode = "404", description = "Account with provided id not found")
    @PostMapping("/{accountId}")
    public Mono<ResponseEntity<TransactionDTO>> registerTransaction(
            @PathVariable String accountId,
            @Valid @RequestBody TransactionDTO transactionDTO) {

        return transactionService.registerTransaction(accountId, transactionDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}