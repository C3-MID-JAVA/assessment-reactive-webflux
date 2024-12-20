package com.sofkau.usrv_accounts_manager.controller;


import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.dto.TransactionDTO;
import com.sofkau.usrv_accounts_manager.services.AccountService;
import com.sofkau.usrv_accounts_manager.services.CardService;
import com.sofkau.usrv_accounts_manager.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transaction creation", description = "Operations related to transactions creation")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create a new transaction", description = "This endpoint allows the user to create a new transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created transaction",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Invalid transaction details")
    })
    @PostMapping("/make")
    public Mono<ResponseEntity<TransactionDTO>> makeTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {

        return transactionService.createTransaction(transactionDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
