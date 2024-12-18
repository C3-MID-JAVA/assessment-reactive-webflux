package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.exception.ErrorResponse;
import com.bankmanagement.bankmanagement.service.TransactionService;
import com.bankmanagement.bankmanagement.util.RequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TransactionRouter {

    private final TransactionService transactionService;
    private final RequestValidator requestValidator;

    public TransactionRouter(TransactionService transactionService, RequestValidator requestValidator) {
        this.transactionService = transactionService;
        this.requestValidator = requestValidator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/transactions",
                    operation = @Operation(
                            tags = {"Transactions"},
                            operationId = "create",
                            summary = "Create a new transaction",
                            description = "This endpoint allows you to create a new transaction. It calculates fees and updates the account balance based on the transaction type.",
                            requestBody = @RequestBody(
                                    description = "Transaction creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TransactionRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Transaction successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, invalid transaction data (e.g., invalid amount, missing account number)",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Account not found, the provided account number does not exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Insufficient balance for this transaction",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/transactions/{accountNumber}/account",
                    operation = @Operation(
                            tags = {"Transactions"},
                            operationId = "getAllByUserId",
                            summary = "Get all transactions for an account",
                            description = "This endpoint retrieves all transactions associated with a specific account number. If the account does not exist, it returns a 404 Not Found error.",
                            parameters = {
                                    @Parameter(
                                            name = "accountNumber",
                                            description = "The account number to retrieve transactions for",
                                            required = true,
                                            in = ParameterIn.PATH
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved transactions for the given account number",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Account not found, the provided account number does not exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> transactionRoutes(){
        return RouterFunctions
                .route(RequestPredicates.POST("/transactions").and(accept(MediaType.APPLICATION_JSON)), this::create)
                .andRoute(RequestPredicates.GET("/transactions/{accountNumber}/account"), this::getAllByUserId);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(TransactionRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(transactionService::create)
                .flatMap(transactionResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionResponseDTO));
    }

    public Mono<ServerResponse> getAllByUserId(ServerRequest request){
        String accountNumber = request.pathVariable("accountNumber");

        return transactionService.getAllByAccountNumber(accountNumber)
                .collectList()
                .flatMap(transactionResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(transactionResponseDTOs));
    }
}
