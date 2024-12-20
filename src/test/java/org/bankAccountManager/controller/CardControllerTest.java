package org.bankAccountManager.controller;

import org.bankAccountManager.DTO.request.CardRequestDTO;
import org.bankAccountManager.DTO.response.CardResponseDTO;
import org.bankAccountManager.entity.Card;
import org.bankAccountManager.service.implementations.CardServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = CardController.class)
class CardControllerTest {

    /*@Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CardServiceImplementation cardService;

    private CardRequestDTO cardRequest;
    private CardResponseDTO cardResponse;
    private Card card;

    @BeforeEach
    void setUp() {
        cardRequest = new CardRequestDTO();
        cardRequest.setId(1);
        cardRequest.setCardNumber("1234567890123456");
        cardRequest.setCardType("Credit");

        cardResponse = new CardResponseDTO();
        cardResponse.setId(1);
        cardResponse.setCardNumber("1234567890123456");
        cardResponse.setCardType("Credit");

        card = new Card();
        card.setId(1);
        card.setCardNumber("1234567890123456");
        card.setCardType("Credit");
    }

    @Test
    void createCard_success() {
        Mockito.when(cardService.createCard(any(Card.class))).thenReturn(Mono.just(card));

        webTestClient.post()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CardResponseDTO.class)
                .isEqualTo(cardResponse);
    }

    @Test
    void createCard_invalidInput() {
        webTestClient.post()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CardRequestDTO())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getCardById_success() {
        Mockito.when(cardService.getCardById(eq(1))).thenReturn(Mono.just(card));

        webTestClient.get()
                .uri("/cards/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isFound()
                .expectBody(CardResponseDTO.class)
                .isEqualTo(cardResponse);
    }

    @Test
    void getCardById_notFound() {
        Mockito.when(cardService.getCardById(eq(1))).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/cards/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllCards_success() {
        Mockito.when(cardService.getAllCards()).thenReturn(Flux.just(card));

        webTestClient.get()
                .uri("/cards")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CardResponseDTO.class)
                .hasSize(1)
                .contains(cardResponse);
    }

    @Test
    void updateCard_success() {
        Mockito.when(cardService.updateCard(any(Card.class))).thenReturn(Mono.just(card));

        webTestClient.put()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CardResponseDTO.class)
                .isEqualTo(cardResponse);
    }

    @Test
    void deleteCard_success() {
        Mockito.when(cardService.deleteCard(eq(1))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteCard_notFound() {
        Mockito.when(cardService.deleteCard(eq(1))).thenReturn(Mono.error(new RuntimeException("Card not found")));

        webTestClient.delete()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isNotFound();
    }*/
}
