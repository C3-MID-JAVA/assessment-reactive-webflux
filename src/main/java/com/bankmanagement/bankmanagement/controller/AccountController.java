package com.bankmanagement.bankmanagement.controller;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.ErrorResponse;
import com.bankmanagement.bankmanagement.service.AccountService;
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
import java.util.UUID;

@Tag(name = "Accounts", description = "Operations related to accounts")
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Create a new account",
            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Account successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, validation error or missing required fields",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The provided user ID does not exist in the system.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        AccountResponseDTO accountResponseDTO = accountService.create(accountRequestDTO);
        return new ResponseEntity<>(accountResponseDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all accounts by user ID",
            description = "Fetch all bank accounts associated with a specific user ID. This endpoint returns a list of accounts or an error if the user ID is invalid or missing.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the list of accounts",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, user ID is missing or invalid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{userId}/user")
    public ResponseEntity<List<AccountResponseDTO>> getAllByUserId(
            @Parameter(description = "The user ID to retrieve associated accounts", required = true)
            @PathVariable String userId
    ){
        List<AccountResponseDTO> accountResponseDTOS = accountService.getAllByUserId(userId);
        return ResponseEntity.ok(accountResponseDTOS);
    }

    @Operation(
            summary = "Get account by account number",
            description = "Fetches the account details associated with the given account number. If the account does not exist, it returns a 404 Not Found error.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the account details",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Account not found. The account number does not exist in the system.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getByAccountNumber(
            @Parameter(description = "The account number to retrieve the account details", required = true)
            @PathVariable String id
    ){
        AccountResponseDTO accountResponseDTO = accountService.findByAccountNumber(id);
        return ResponseEntity.ok(accountResponseDTO);
    }

}
