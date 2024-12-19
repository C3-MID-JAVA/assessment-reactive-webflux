package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.UserRequestDTO;
import com.bankmanagement.bankmanagement.dto.UserResponseDTO;
import com.bankmanagement.bankmanagement.exception.ErrorResponse;
import com.bankmanagement.bankmanagement.service.UserService;
import com.bankmanagement.bankmanagement.util.RequestValidator;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {

    private final UserService userService;
    private final RequestValidator requestValidator;

    public UserRouter(UserService userService, RequestValidator requestValidator) {
        this.userService = userService;
        this.requestValidator = requestValidator;
    }


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/users",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "register",
                            summary = "Register a new user",
                            description = "This endpoint registers a new user in the system. It accepts a request body with user details and returns the created user response with a unique user identifier.",
                            requestBody = @RequestBody(
                                    description = "User registration details",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User successfully created",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, validation error or missing required fields",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request, user already exist",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/users",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "getAllUsers",
                            summary = "Get all users",
                            description = "This endpoint retrieves all users",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully retrieved users",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/users").and(accept(MediaType.APPLICATION_JSON)), this::register)
                .andRoute(RequestPredicates.GET("/users"), this::getAllUsers);
    }


    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .doOnNext(requestValidator::validate)
                .flatMap(userService::register)
                .flatMap(userResponseDTO -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResponseDTO));
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request){
        return userService.getAllUsers()
                .collectList()
                .flatMap(userResponseDTOs ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userResponseDTOs));
    }
}
