package org.bankAccountManager.controller;

import org.bankAccountManager.DTO.request.AccountRequestDTO;
import org.bankAccountManager.DTO.request.TransactionRequestDTO;
import org.bankAccountManager.DTO.response.TransactionResponseDTO;
import org.bankAccountManager.service.implementations.TransactionServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(TransactionController.class)
class TransactionControllerTest {

    /*@Mock
    private TransactionServiceImplementation transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        // Configura WebTestClient con el controlador
        webTestClient = bindToController(transactionController).build();
    }

    // Prueba positiva
    @Test
    void createTransaction_ShouldReturnCreated() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId("transaction-id");

        when(transactionService.createTransaction(any()))
                .thenReturn(Mono.just(responseDTO));

        webTestClient.post()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionResponseDTO.class)
                .value(response -> assertThat(response.getId()).isEqualTo("transaction-id"));

        verify(transactionService, times(1)).createTransaction(any());
    }

    // Prueba negativa: Entrada inválida (por ejemplo, monto nulo)
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenInvalidInput() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        // Sin monto, lo cual es inválido

        webTestClient.post()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest();

        verify(transactionService, never()).createTransaction(any());
    }

    // Prueba positiva
    @Test
    void getTransactionById_ShouldReturnTransaction() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId("transaction-id");

        when(transactionService.getTransactionById(any()))
                .thenReturn(Mono.just(responseDTO));

        webTestClient.get()
                .uri("/transaction/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isFound()
                .expectBody(TransactionResponseDTO.class)
                .value(response -> assertThat(response.getId()).isEqualTo("transaction-id"));

        verify(transactionService, times(1)).getTransactionById(any());
    }

    // Prueba negativa: Transacción no encontrada
    @Test
    void getTransactionById_ShouldReturnNotFound_WhenTransactionDoesNotExist() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        when(transactionService.getTransactionById(any()))
                .thenReturn(Mono.empty());  // Transacción no encontrada

        webTestClient.get()
                .uri("/transaction/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isNotFound();

        verify(transactionService, times(1)).getTransactionById(any());
    }

    // Prueba positiva
    @Test
    void getAllTransactions_ShouldReturnListOfTransactions() {
        TransactionResponseDTO responseDTO1 = new TransactionResponseDTO();
        responseDTO1.setId("transaction-id-1");

        TransactionResponseDTO responseDTO2 = new TransactionResponseDTO();
        responseDTO2.setId("transaction-id-2");

        when(transactionService.getAllTransactions())
                .thenReturn(Flux.just(responseDTO1, responseDTO2));

        webTestClient.get()
                .uri("/transaction")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionResponseDTO.class)
                .hasSize(2)
                .value(transactions -> {
                    assertThat(transactions.get(0).getId()).isEqualTo("transaction-id-1");
                    assertThat(transactions.get(1).getId()).isEqualTo("transaction-id-2");
                });

        verify(transactionService, times(1)).getAllTransactions();
    }

    // Prueba negativa: Error en el servicio al obtener todas las transacciones
    @Test
    void getAllTransactions_ShouldReturnInternalServerError_WhenServiceFails() {
        when(transactionService.getAllTransactions())
                .thenReturn(Flux.error(new RuntimeException("Service error")));

        webTestClient.get()
                .uri("/transaction")
                .exchange()
                .expectStatus().is5xxServerError();

        verify(transactionService, times(1)).getAllTransactions();
    }

    // Prueba positiva
    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(200.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId("updated-transaction-id");

        when(transactionService.updateTransaction(any()))
                .thenReturn(Mono.just(responseDTO));

        webTestClient.put()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionResponseDTO.class)
                .value(response -> assertThat(response.getId()).isEqualTo("updated-transaction-id"));

        verify(transactionService, times(1)).updateTransaction(any());
    }

    // Prueba negativa: Error al actualizar transacción (por ejemplo, transacción no encontrada)
    @Test
    void updateTransaction_ShouldReturnNotFound_WhenTransactionDoesNotExist() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(200.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        when(transactionService.updateTransaction(any()))
                .thenReturn(Mono.empty());  // Transacción no encontrada

        webTestClient.put()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isNotFound();

        verify(transactionService, times(1)).updateTransaction(any());
    }

    // Prueba negativa: Error en el servicio al eliminar una transacción
    @Test
    void deleteTransaction_ShouldReturnInternalServerError_WhenServiceFails() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSourceAccountId("source-account-id");
        requestDTO.setDestinationAccountId("destination-account-id");

        when(transactionService.deleteTransaction(any()))
                .thenReturn(Mono.error(new RuntimeException("Service error")));  // Simulando un fallo en el servicio

        webTestClient.delete()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(transactionService, times(1)).deleteTransaction(any());
    }

    // Prueba negativa: Entrada inválida al eliminar transacción (sin ID)
    @Test
    void deleteTransaction_ShouldReturnBadRequest_WhenInvalidInput() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        // Sin ID de transacción, lo que es inválido

        webTestClient.delete()
                .uri("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest();

        verify(transactionService, never()).deleteTransaction(any());
    }*/
}
