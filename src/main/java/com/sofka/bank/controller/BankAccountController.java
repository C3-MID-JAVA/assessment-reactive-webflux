package com.sofka.bank.controller;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Get all accounts", description = "Retrieve all accounts with their respective details")
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No content not found")
    @GetMapping
    public Mono<ResponseEntity<Flux<BankAccountDTO>>> getAllAccounts(){
        Flux<BankAccountDTO> accounts = bankAccountService.getAllAccounts();
        return accounts.collectList()
                .flatMap(accountList -> accountList.isEmpty()
                        ? Mono.just(ResponseEntity.noContent().build())
                        : Mono.just(ResponseEntity.ok(Flux.fromIterable(accountList)))
                );
    }

    @Operation(summary = "Create a new account", description = "Creates a new account in the system.")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public Mono<ResponseEntity<BankAccountDTO>> createAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO) {
        return bankAccountService.createAccount(bankAccountDTO)
                .map(createdAccount -> ResponseEntity.status(201).body(createdAccount))
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .defaultIfEmpty(ResponseEntity.status(400).build());
    }
}
