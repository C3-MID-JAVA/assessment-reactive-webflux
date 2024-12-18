package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.ErrorResponse;
import com.bankmanagement.bankmanagement.service.AccountService;
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
public class AccountRouter {

    private final AccountService accountService;
    private final RequestValidator requestValidator;

    public AccountRouter(AccountService accountService, RequestValidator requestValidator) {
        this.accountService = accountService;
        this.requestValidator = requestValidator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/accounts",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "create",
                            summary = "Create a new account",
                            description = "This endpoint allows the creation of a new bank account for a user. It accepts user details in the request body and returns the created account's information.",
                            requestBody = @RequestBody(
                                    description = "Account creation details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = AccountRequestDTO.class)
                                    )
                            ),
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
            ),
            @RouterOperation(
                    path = "/accounts/{userId}/user",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getAllByUserId",
                            summary = "Get all accounts by user ID",
                            description = "Fetch all bank accounts associated with a specific user ID. This endpoint returns a list of accounts or an error if the user ID is invalid or missing.",
                            parameters = {
                                    @Parameter(
                                            name = "userId",
                                            description = "The user ID to retrieve associated accounts",
                                            required = true,
                                            in = ParameterIn.PATH
                                    )
                            },
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
            ),
            @RouterOperation(
                    path = "/accounts/{id}",
                    operation = @Operation(
                            tags = {"Accounts"},
                            operationId = "getByAccountNumber",
                            summary = "Get account by account number",
                            description = "Fetches the account details associated with the given account number. If the account does not exist, it returns a 404 Not Found error.",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "The account number to retrieve account info",
                                            required = true,
                                            in = ParameterIn.PATH
                                    )
                            },
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
            )
    })
    public RouterFunction<ServerResponse> accountRoutes(){
        return RouterFunctions
                .route(RequestPredicates.POST("/accounts").and(accept(MediaType.APPLICATION_JSON)), this::create)
                .andRoute(RequestPredicates.GET("/accounts/{userId}/user"), this::getAllByUserId)
                .andRoute(RequestPredicates.GET("/accounts/{id}"), this::getByAccountNumber);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(accountService::create)
                .flatMap(accountResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountResponseDTO));
    }

    public Mono<ServerResponse> getAllByUserId(ServerRequest request){
        String userId = request.pathVariable("userId");

        return accountService.getAllByUserId(userId)
                .collectList()
                .flatMap(accountResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(accountResponseDTOs));
    }

    public Mono<ServerResponse> getByAccountNumber(ServerRequest request){
        String accountNumber = request.pathVariable("id");

        return accountService.findByAccountNumber(accountNumber)
                .flatMap(accountResponseDTO ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(accountResponseDTO));

    }
}
