package com.sofkau.usrv_accounts_manager.controller;


import com.sofkau.usrv_accounts_manager.dto.AccountSimpleRequestDTO;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.services.CardService;
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
@RequestMapping("/api/v1/card")
@Tag(name = "Card Management", description = "Operations related to card management")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(
            summary = "Create a new card",
            description = "Creates a new card with the provided details about client and account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created account", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public Mono<ResponseEntity<CardDTO>> createAccount(@Valid @RequestBody CardDTO cardDTO) {

        return cardService.createCard(cardDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @PostMapping("/byAccount")
    public Mono<ResponseEntity<Flux<CardDTO>>> findByAccount(@Valid @RequestBody AccountSimpleRequestDTO accountRequest) {

        return cardService.getCardsByAccount(accountRequest.getAccountNumber())
                .collectList()
                .flatMap(elements -> elements.isEmpty()
                        ? Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                        : Mono.just(ResponseEntity.status(HttpStatus.OK).body(Flux.fromIterable(elements))
                ));
    }
}
