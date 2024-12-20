package com.reactivo.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.reactivo.banco.model.dto.CardInDTO;
import com.reactivo.banco.model.dto.CardOutDTO;
import com.reactivo.banco.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private CardInDTO cardInDTO;
    private CardOutDTO cardOutDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cardInDTO = new CardInDTO("123456789", "Debit", "accountId123");
        cardOutDTO = new CardOutDTO("1", "123456789", "Debit");
    }

    @Test
    void shouldCreateCardSuccessfully() {
        when(cardService.crearTarjeta(any(CardInDTO.class))).thenReturn(Mono.just(cardOutDTO));

        Mono<ResponseEntity<CardOutDTO>> response = cardController.crearTarjeta(cardInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    assertEquals("123456789", res.getBody().getCardNumber());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnAllCardsSuccessfully() {
        when(cardService.obtenerTodasLasTarjetas()).thenReturn(Flux.just(cardOutDTO));

        Mono<ResponseEntity<Flux<CardOutDTO>>> response = cardController.obtenerTodasLasTarjetas();

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertTrue(res.getBody().collectList().block().size() > 0);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnCardByIdSuccessfully() {
        when(cardService.obtenerTarjetaPorId(anyString())).thenReturn(Mono.just(cardOutDTO));

        Mono<ResponseEntity<CardOutDTO>> response = cardController.obtenerTarjetaPorId("1");

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    void shouldUpdateCardSuccessfully() {
        when(cardService.actualizarTarjeta(anyString(), any(CardInDTO.class))).thenReturn(Mono.just(cardOutDTO));

        Mono<ResponseEntity<CardOutDTO>> response = cardController.actualizarTarjeta("1", cardInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    void shouldDeleteCardSuccessfully() {
        when(cardService.eliminarTarjeta(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = cardController.eliminarTarjeta("1");

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
                    return true;
                })
                .verifyComplete();
    }


}
