package com.reactivo.banco.controller;

import com.reactivo.banco.model.dto.CardInDTO;
import com.reactivo.banco.model.dto.CardOutDTO;
import com.reactivo.banco.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/tarjetas")
@Tag(name = "Cards", description = "Manage card services")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(
            summary = "Create Card",
            description = "Endpoint to create a new card.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardOutDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping
    public Mono<CardOutDTO> crearTarjeta(@Valid @RequestBody CardInDTO tarjetaInDTO) {
        return cardService.crearTarjeta(tarjetaInDTO);
    }

    @Operation(
            summary = "Get All Cards",
            description = "Retrieve a list of all registered cards.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardOutDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping
    public Flux<CardOutDTO> obtenerTodasLasTarjetas() {
        return cardService.obtenerTodasLasTarjetas();
    }

    @Operation(
            summary = "Get Card by ID",
            description = "Retrieve a specific card by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardOutDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public Mono<CardOutDTO> obtenerTarjetaPorId(@PathVariable String id) {
        return cardService.obtenerTarjetaPorId(id);
    }

    @Operation(
            summary = "Update Card",
            description = "Update details of a specific card by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardOutDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public Mono<CardOutDTO> actualizarTarjeta(@PathVariable String id, @Valid @RequestBody CardInDTO tarjetaInDTO) {
        return cardService.actualizarTarjeta(id, tarjetaInDTO);
    }

    @Operation(
            summary = "Delete Card",
            description = "Delete a specific card by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public Mono<Void> eliminarTarjeta(@PathVariable String id) {
        return cardService.eliminarTarjeta(id);
    }
}
