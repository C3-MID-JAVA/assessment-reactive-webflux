package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Account RESTful", description = "Endpoints for account management.")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create new account", description = "Create a new account from the request data.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully."),
            @ApiResponse(responseCode = "404", description = "No resources found."),
            @ApiResponse(responseCode = "500", description = "Internal application problems.")
    })
    @PostMapping
    public Mono<ResponseEntity<AccountResponseDTO>> createAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Body with the account data to be created", required = true)
            @Valid @RequestBody AccountRequestDTO account) {
        return accountService.createAccount(account)
                .map(accountResponse -> ResponseEntity.status(HttpStatus.CREATED).body(accountResponse));
    }

    @Operation(summary = "Get all accounts", description = "Get all registered accounts.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully obtained all registered accounts."),
            @ApiResponse(responseCode = "404", description = "No resources found."),
            @ApiResponse(responseCode = "500", description = "Internal application problems.")
    })
    @GetMapping
    public Mono<ResponseEntity<List<AccountResponseDTO>>> getAccounts() {
        return accountService.getAllAccounts()
                .collectList()
                .map(ResponseEntity::ok);
    }
}