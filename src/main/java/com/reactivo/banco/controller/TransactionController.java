package com.reactivo.banco.controller;

import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import com.reactivo.banco.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/movimientos")
@Tag(name = "Transactions", description = "Manage transaction operations")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposito/sucursal")
    @Operation(
            summary = "Make Branch Deposit",
            description = "Endpoint for making a deposit at a physical branch.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch deposit successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makeBranchDeposit(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makeBranchDeposit(transactionInDTO);
    }

    @PostMapping("/deposito/cajero")
    @Operation(
            summary = "Make ATM Deposit",
            description = "Endpoint for making a deposit via ATM.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ATM deposit successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makeATMDeposit(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makeATMDeposit(transactionInDTO);
    }


    @PostMapping("/deposito/otra-cuenta")
    @Operation(
            summary = "Deposit to Another Account",
            description = "Endpoint for depositing to another account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deposit to another account successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makeDepositToAnotherAccount(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makeDepositToAnotherAccount(transactionInDTO);
    }

    @PostMapping("/compra/fisica")
    @Operation(
            summary = "Make Physical Purchase",
            description = "Endpoint for making a physical purchase transaction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Physical purchase successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makePhysicalPurchase(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makePhysicalPurchase(transactionInDTO);
    }

    @PostMapping("/compra/web")
    @Operation(
            summary = "Make Online Purchase",
            description = "Endpoint for making an online purchase transaction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Online purchase successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makeOnlinePurchase(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makeOnlinePurchase(transactionInDTO);
    }

    @PostMapping("/retiro/cajero")
    @Operation(
            summary = "Make ATM Withdrawal",
            description = "Endpoint for withdrawing money through ATM.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ATM withdrawal successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    public Mono<TransactionOutDTO> makeATMWithdrawal(@RequestBody TransactionInDTO transactionInDTO) {
        return transactionService.makeATMWithdrawal(transactionInDTO);
    }
}
