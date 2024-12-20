package org.bankAccountManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bankAccountManager.DTO.request.AccountRequestDTO;
import org.bankAccountManager.DTO.request.CustomerRequestDTO;
import org.bankAccountManager.DTO.response.AccountResponseDTO;
import org.bankAccountManager.entity.Account;
import org.bankAccountManager.service.implementations.AccountServiceImplementation;
import org.bankAccountManager.service.interfaces.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.bankAccountManager.mapper.DTORequestMapper.toAccount;
import static org.bankAccountManager.mapper.DTOResponseMapper.toAccountResponseDTO;

@Tag(name = "Account Management", description = "Endpoints for managing accounts")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountServiceImplementation accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account", description = "Create a new account with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Mono<ResponseEntity<AccountResponseDTO>> createAccount(@Valid @RequestBody AccountRequestDTO account) {
        return accountService.createAccount(toAccount(Mono.just(account)))
                .flatMap(accountEntity -> toAccountResponseDTO(Mono.just(accountEntity))
                        .map(accountResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(accountResponseDTO)));
    }

    @Operation(summary = "Retrieve an account by ID", description = "Get the details of an account using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "302", description = "Account retrieved successfully")
    })
    @PostMapping("/id")
    public Mono<ResponseEntity<AccountResponseDTO>> getAccountById(@Valid @RequestBody AccountRequestDTO account) {
        return accountService.getAccountById(toAccount(Mono.just(account))
                        .map(Account::getId))
                .flatMap(accountEntity -> toAccountResponseDTO(Mono.just(accountEntity))
                        .map(accountResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(accountResponseDTO)));
    }

    @Operation(summary = "Retrieve an account by Customer ID", description = "Get the details of an account using the customer's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "302", description = "Account not found")
    })
    @PostMapping("/customer")
    public Mono<ResponseEntity<Flux<AccountResponseDTO>>> getAccountsByCustomerId(@Valid @RequestBody CustomerRequestDTO customer) {
        return Mono.just(ResponseEntity.status(HttpStatus.FOUND).body(
                accountService.getAccountsByCustomerId(Mono.just(customer.getId()))
                        .flatMap(accountEntity -> toAccountResponseDTO(Mono.just(accountEntity)))
        ));
    }

    @Operation(summary = "Retrieve all accounts", description = "Get the list of all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<AccountResponseDTO>>> getAllAccounts() {
        return Mono.just(
                ResponseEntity.ok(
                        accountService.getAllAccounts()
                                .flatMap(accountEntity -> toAccountResponseDTO(Mono.just(accountEntity)))
                )
        );
    }

    @Operation(summary = "Update an account", description = "Update an existing account with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping
    public Mono<ResponseEntity<AccountResponseDTO>> updateAccount(@Valid @RequestBody AccountRequestDTO account) {
        return accountService.updateAccount(toAccount(Mono.just(account)))
                .flatMap(accountEntity -> toAccountResponseDTO(Mono.just(accountEntity))
                        .map(accountResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(accountResponseDTO)));
    }

    @Operation(summary = "Delete an account", description = "Delete an account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteAccount(@Valid @RequestBody AccountRequestDTO account) {
        return toAccount(Mono.just(account)) // Convierte el DTO a la entidad Account
                .flatMap(accountEntity ->
                        accountService.deleteAccount(Mono.just(accountEntity.getId()))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()))
                );
    }
}