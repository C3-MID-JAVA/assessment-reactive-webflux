package com.sofkau.usrv_accounts_manager.controller;


import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.services.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Validated
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account Management", description = "Operations related to account management")
public class AccountController {
    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Create a new account",
            description = "Creates a new account with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created account", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public Mono<ResponseEntity<AccountDTO>> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO)
                .map(createdAcc -> ResponseEntity.ok().body(createdAcc))
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @Operation(
            summary = "Retrieve al accounts",
            description = "Retrieve all accounts in database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Accounts not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping()
    public Mono<ResponseEntity<Flux<AccountDTO>>> getAllAccounts() {

        return accountService.getAllAccounts()
                .collectList()
                .flatMap(elements ->
                        elements.isEmpty() ?
                                Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                                : Mono.just(ResponseEntity.status(HttpStatus.OK).body(Flux.fromIterable(elements)))

                );
    }
}
