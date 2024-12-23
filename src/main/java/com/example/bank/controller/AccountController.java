package com.example.bank.controller;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import com.example.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Account endpoints", description = "")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get Accounts", description = "List all bank accounts.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successfully obtained all accounts.",
                    content = @Content(mediaType = "application/json", schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "204", description = "No accounts to get.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request. Validation error. Missing fields?",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public Mono<ResponseEntity<List<AccountResponseDTO>>> getAccounts() {
        return accountService.getAllAccounts()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get account by id", description = "Find a single bank account by its id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successfully obtained account.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Account not found.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request. Validation error. Missing fields?",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponseDTO>> getAccountById(
            @Parameter(description = "ID of the account to retrieve", required = true, example = "3040cf52-")
            @PathVariable String id
    ) {
        return accountService.getAccountById(id)
                .map(res -> ResponseEntity.status(200).body(res));
    }

    @Operation(summary = "Create new account.", description = "Create a new bank account.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Successfully created account.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request. Validation error. Missing fields?",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public Mono<ResponseEntity<AccountResponseDTO>> createAccount(@Valid @RequestBody AccountRequestDTO accountDTO) {
        return accountService.createAccount(accountDTO)
                .map(res -> ResponseEntity.status(201).body(res));
    }
}