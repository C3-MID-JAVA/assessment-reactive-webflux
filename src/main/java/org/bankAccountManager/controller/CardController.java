package org.bankAccountManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bankAccountManager.DTO.request.AccountRequestDTO;
import org.bankAccountManager.DTO.request.CardRequestDTO;
import org.bankAccountManager.DTO.response.CardResponseDTO;
import org.bankAccountManager.entity.Card;
import org.bankAccountManager.service.implementations.CardServiceImplementation;
import org.bankAccountManager.service.interfaces.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.bankAccountManager.mapper.DTORequestMapper.toAccount;
import static org.bankAccountManager.mapper.DTORequestMapper.toCard;
import static org.bankAccountManager.mapper.DTOResponseMapper.toCardResponseDTO;

@Tag(name = "Card Management", description = "Endpoints for managing cards")
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardServiceImplementation cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Create a new card", description = "Add a new card to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Mono<ResponseEntity<CardResponseDTO>> createCard(@RequestBody CardRequestDTO card) {
        return cardService.createCard(toCard(Mono.just(card)))
                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity))
                        .map(cardResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(cardResponseDTO)));
    }

    @Operation(summary = "Retrieve a card by ID", description = "Get details of a card by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Card retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/id")
    public Mono<ResponseEntity<CardResponseDTO>> getCardById(@RequestBody CardRequestDTO card) {
        return cardService.getCardById(toCard(Mono.just(card))
                        .map(Card::getId))
                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity))
                        .map(cardResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(cardResponseDTO)));
    }

    @Operation(summary = "Retrieve a card by number", description = "Get details of a card by its unique number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Card retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/number")
    public Mono<ResponseEntity<CardResponseDTO>> getCardByNumber(@RequestBody CardRequestDTO card) {
        return cardService.getCardByNumber(toCard(Mono.just(card))
                        .map(Card::getCardNumber))
                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity))
                        .map(cardResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(cardResponseDTO)));
    }

    @Operation(summary = "Retrieve all cards", description = "Get a list of all available cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards retrieved successfully")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<CardResponseDTO>>> getAllCards() {
        return Mono.just(
                ResponseEntity.ok(
                        cardService.getAllCards()
                                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity)))
                )
        );
    }

    @Operation(summary = "Retrieve cards by account ID", description = "Get all cards associated with a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Cards retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/account")
    public Mono<ResponseEntity<Flux<CardResponseDTO>>> getCardsByAccount(@RequestBody AccountRequestDTO account) {
        return cardService.getCardsByAccount(toAccount(Mono.just(account)))
                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity)))
                .collectList()
                .map(cardDTOs -> ResponseEntity.status(HttpStatus.FOUND).body(Flux.fromIterable(cardDTOs))
                );
    }

    @Operation(summary = "Retrieve cards by type", description = "Get all cards of a specific type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Cards retrieved successfully")
    })
    @PostMapping("/type")
    public Mono<ResponseEntity<Flux<CardResponseDTO>>> getCardsByType(@RequestBody CardRequestDTO card) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.FOUND).body(
                        cardService.getCardsByType(Mono.just(card.getCardType()))
                                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity)))
                )
        );
    }

    @Operation(summary = "Update a card", description = "Update details of an existing card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PutMapping
    public Mono<ResponseEntity<CardResponseDTO>> updateCard(@RequestBody CardRequestDTO card) {
        return cardService.updateCard(toCard(Mono.just(card)))
                .flatMap(cardEntity -> toCardResponseDTO(Mono.just(cardEntity))
                        .map(cardResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(cardResponseDTO)));
    }

    @Operation(summary = "Delete a card", description = "Remove a card from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteCard(@RequestBody CardRequestDTO card) {
        return toCard(Mono.just(card)) // Convierte el DTO a la entidad Account
                .flatMap(cardEntity ->
                        cardService.deleteCard(Mono.just(cardEntity.getId()))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()))
                );
    }
}