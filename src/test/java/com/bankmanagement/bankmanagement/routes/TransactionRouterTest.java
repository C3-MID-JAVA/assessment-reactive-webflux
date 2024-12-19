package com.bankmanagement.bankmanagement.routes;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.model.TransactionType;
import com.bankmanagement.bankmanagement.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TransactionRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    private TransactionRequestDTO validTransactionRequest;
    private TransactionResponseDTO transactionResponse;

    @BeforeEach
    void setUp(){
        validTransactionRequest = new TransactionRequestDTO(100.0, TransactionType.ATM_DEPOSIT, "123456789");
        transactionResponse = new TransactionResponseDTO("675e0ec661737976b43cca86", 2.0, 98.0, TransactionType.ATM_DEPOSIT, LocalDateTime.now());
    }

    @Test
    void create_validTransaction_ReturnsCreatedResponse() {
        when(transactionService.create(any(TransactionRequestDTO.class))).thenReturn(Mono.just(transactionResponse));

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$.fee").isEqualTo(2.0)
                .jsonPath("$.netAmount").isEqualTo(98.0)
                .jsonPath("$.type").isEqualTo("ATM_DEPOSIT");

        verify(transactionService, times(1)).create(any(TransactionRequestDTO.class));
    }

    @Test
    void create_accountNotFound_ReturnsNotFound() {
        when(transactionService.create(any(TransactionRequestDTO.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validTransactionRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(transactionService, times(1)).create(any(TransactionRequestDTO.class));
    }

    @Test
    void create_invalidTransactionData_ReturnsBadRequest() {
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO(-100.0, TransactionType.ATM_DEPOSIT, "123456789");

        webTestClient.post().uri("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidTransactionRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(transactionService, never()).create(any(TransactionRequestDTO.class));
    }

    @Test
    void getAllByAccountNumber_validAccount_ReturnsTransactionList() {
        List<TransactionResponseDTO> transactionList = List.of(transactionResponse);

        when(transactionService.getAllByAccountNumber(anyString())).thenReturn(Flux.fromIterable(transactionList));

        webTestClient.get().uri("/transactions/{accountNumber}/account", "123456789")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("675e0ec661737976b43cca86")
                .jsonPath("$[0].fee").isEqualTo(2.0)
                .jsonPath("$[0].netAmount").isEqualTo(98.0)
                .jsonPath("$[0].type").isEqualTo("ATM_DEPOSIT");

        verify(transactionService, times(1)).getAllByAccountNumber("123456789");
    }

    @Test
    void getAllByAccountNumber_accountNotFound_ReturnsNotFound() {
        when(transactionService.getAllByAccountNumber(anyString()))
                .thenReturn(Flux.error(new NotFoundException("Account not found")));

        webTestClient.get().uri("/transactions/{accountNumber}/account", "99999999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Account not found");

        verify(transactionService, times(1)).getAllByAccountNumber("99999999");
    }
}
