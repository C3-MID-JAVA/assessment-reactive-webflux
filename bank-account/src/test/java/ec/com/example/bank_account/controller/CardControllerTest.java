package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.CardRequestDTO;
import ec.com.example.bank_account.dto.CardResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private AccountResponseDTO accountResponseDTO;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(cardController).build();

        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");
        TypeAccountResponseDTO typeAccountResponse = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");
        accountResponseDTO = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponse));
    }

    @Test
    void createCard_ShouldReturnCreatedStatus_WhenCardIsCreatedSuccessfully() {
        CardRequestDTO cardRequest = new CardRequestDTO("Diego Loor", new BigDecimal(1000),
                "234", new Date(), "ACTIVE", "test12");
        CardResponseDTO cardResponse = new CardResponseDTO("2200000000", new BigDecimal(100),
                "234", new Date(), null, Mono.just(accountResponseDTO));
        when(cardService.createCard(any(CardRequestDTO.class))).thenReturn(Mono.just(cardResponse));

        webTestClient.post()
                .uri("/api/cards")
                .bodyValue(cardRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.cvcCode").isEqualTo("234");

        verify(cardService).createCard(any(CardRequestDTO.class));
    }

    @Test
    void getAllCards_ShouldReturnOkStatus_WhenCardsAreRetrievedSuccessfully() {
        CardResponseDTO cards = new CardResponseDTO("Diego Loor", new BigDecimal(1000),
                "234", new Date(), "ACTIVE", Mono.just(accountResponseDTO));
        when(cardService.getAllCards()).thenReturn(Flux.just(cards));

        webTestClient.get()
                .uri("/api/cards")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponseDTO.class)
                .hasSize(1);

        verify(cardService).getAllCards();
    }

}