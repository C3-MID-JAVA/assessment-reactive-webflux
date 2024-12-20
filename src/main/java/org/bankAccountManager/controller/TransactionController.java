package org.bankAccountManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bankAccountManager.DTO.request.AccountRequestDTO;
import org.bankAccountManager.DTO.request.BranchRequestDTO;
import org.bankAccountManager.DTO.request.TransactionRequestDTO;
import org.bankAccountManager.DTO.response.TransactionResponseDTO;
import org.bankAccountManager.entity.Transaction;
import org.bankAccountManager.service.implementations.TransactionServiceImplementation;
import org.bankAccountManager.service.interfaces.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.bankAccountManager.mapper.DTORequestMapper.*;
import static org.bankAccountManager.mapper.DTOResponseMapper.toTransactionResponseDTO;

@Tag(name = "Transaction Management", description = "Endpoints for managing transactions")
@RestController
@RequestMapping("/transaction")
public class TransactionController {


    private final TransactionService transactionService;

    public TransactionController(TransactionServiceImplementation transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create a new transaction", description = "Add a new transaction to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Mono<ResponseEntity<TransactionResponseDTO>> createTransaction(@RequestBody TransactionRequestDTO transaction) {
        return transactionService.createTransaction(toTransaction(Mono.just(transaction)))
                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity))
                        .map(transactionResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDTO)));
    }

    @Operation(summary = "Retrieve a transaction by ID", description = "Get details of a transaction by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping("/id")
    public Mono<ResponseEntity<TransactionResponseDTO>> getTransactionById(@RequestBody TransactionRequestDTO transaction) {
        return transactionService.getTransactionById(toTransaction(Mono.just(transaction))
                        .map(Transaction::getId))
                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity))
                        .map(transactionResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(transactionResponseDTO)));
    }

    @Operation(summary = "Retrieve all transactions", description = "Get a list of all transactions in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<TransactionResponseDTO>>> getAllTransactions() {
        return Mono.just(
                ResponseEntity.ok(
                        transactionService.getAllTransactions()
                                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity)))
                )
        );
    }

    @Operation(summary = "Retrieve transactions by branch ID", description = "Get a list of transactions for a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PostMapping("/branch")
    public Mono<ResponseEntity<TransactionResponseDTO>> getTransactionByBranch(@RequestBody BranchRequestDTO branch) {
        return transactionService.getTransactionByBranch(toBranch(Mono.just(branch)))
                .flatMap(transactionEntity ->
                        toTransactionResponseDTO(Mono.just(transactionEntity))
                                .map(responseDTO ->
                                        ResponseEntity.status(HttpStatus.FOUND).body(responseDTO)
                                )
                );
    }

    @Operation(summary = "Retrieve transactions by destination account ID", description = "Get transactions linked to a specific destination account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/account/destination")
    public Mono<ResponseEntity<Flux<TransactionResponseDTO>>> getTransactionsByDestinationAccountId(@RequestBody AccountRequestDTO account) {
        return transactionService.getTransactionsByDestinationAccount(toAccount(Mono.just(account)))
                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity)))
                .collectList()
                .map(transactionDTOs -> ResponseEntity.status(HttpStatus.FOUND).body(Flux.fromIterable(transactionDTOs))
                );
    }

    @Operation(summary = "Retrieve transactions by source account ID", description = "Get transactions linked to a specific source account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/account/source")
    public Mono<ResponseEntity<Flux<TransactionResponseDTO>>> getTransactionsBySourceAccountId(@RequestBody AccountRequestDTO account) {
        return transactionService.getTransactionsBySourceAccount(toAccount(Mono.just(account)))
                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity)))
                .collectList()
                .map(transactionDTOs -> ResponseEntity.status(HttpStatus.FOUND).body(Flux.fromIterable(transactionDTOs))
                );
    }

    @Operation(summary = "Retrieve transactions by date", description = "Get transactions made on a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transactions retrieved successfully")
    })
    @PostMapping("/date")
    public Mono<ResponseEntity<Flux<TransactionResponseDTO>>> getTransactionsByDate(@RequestBody TransactionRequestDTO transaction) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.FOUND).body(
                        transactionService.getTransactionsByDate(Mono.just(transaction.getDate()))
                                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity)))
                )
        );
    }

    @Operation(summary = "Retrieve transactions by type", description = "Get transactions of a specific type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction type not found")
    })
    @PostMapping("/type")
    public Mono<ResponseEntity<Flux<TransactionResponseDTO>>> getTransactionsByType(@RequestBody TransactionRequestDTO transaction) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.FOUND).body(
                        transactionService.getTransactionsByType(Mono.just(transaction.getType()))
                                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity)))
                )
        );
    }

    @Operation(summary = "Update a transaction", description = "Update details of an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping
    public Mono<ResponseEntity<TransactionResponseDTO>> updateTransaction(@RequestBody TransactionRequestDTO transaction) {
        return transactionService.updateTransaction(toTransaction(Mono.just(transaction)))
                .flatMap(transactionEntity -> toTransactionResponseDTO(Mono.just(transactionEntity))
                        .map(transactionResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO)));
    }

    @Operation(summary = "Delete a transaction", description = "Remove a transaction from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteTransaction(@RequestBody TransactionRequestDTO transaction) {
        return toTransaction(Mono.just(transaction)) // Convierte el DTO a la entidad Account
                .flatMap(transactionEntity ->
                        transactionService.deleteTransaction(Mono.just(transactionEntity.getId()))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()))
                );
    }
}
