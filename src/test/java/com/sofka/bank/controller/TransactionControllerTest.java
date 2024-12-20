package com.sofka.bank.controller;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.dto.TransactionDTO;
import com.sofka.bank.entity.TransactionType;
import com.sofka.bank.exceptions.AccountNotFoundException;
import com.sofka.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private TransactionService transactionService;

    private String accountId;
    private TransactionDTO validTransactionDTO;

    @BeforeEach
    public void setup() {
        accountId = "12345";

        validTransactionDTO = new TransactionDTO("40", TransactionType.WITHDRAW_ATM, 1000.0, 0, LocalDateTime.now(),
                "ATM Withdrawal", new BankAccountDTO(accountId, "1000008", "John Doe", 5000.0, new ArrayList<>()));
    }

    @Test
    @DisplayName("Should return a 200 OK response when retrieving balance for specified account")
    public void testGetGlobalBalance_Success() {
        Mockito.when(transactionService.getGlobalBalance(accountId)).thenReturn(Mono.just(5000.0));

        webTestClient.get().uri("/transactions/balance/{accountId}", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Double.class).isEqualTo(5000.0);
    }

    @Test
    @DisplayName("Should return a 404 Not Found response when account is not found")
    public void testGetGlobalBalance_AccountNotFound() throws Exception {
        Mockito.when(transactionService.getGlobalBalance(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        webTestClient.get().uri("/transactions/balance/{accountId}", accountId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Account not found");
    }

    @Test
    @DisplayName("Should return a 200 OK Response when transaction is successfully registered")
    public void testRegisterTransaction_Success() throws Exception {
        Mockito.when(transactionService.registerTransaction(Mockito.anyString(), Mockito.any(TransactionDTO.class)))
                .thenReturn(Mono.just(validTransactionDTO));

        webTestClient.post().uri("/transactions/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"transactionType\": \"WITHDRAW_ATM\", \"amount\": 1000.0, \"description\": \"ATM Withdrawal\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionDTO.class)
                .value(transaction -> {
                    assertThat(transaction.getDescription()).isEqualTo("ATM Withdrawal");
                    assertThat(transaction.getAmount()).isEqualTo(1000.0);
                });
    }


    @Test
    @DisplayName("Should return 404 Not Found HTTP Response when the account for which transaction is being " +
            "registered is not found")
    public void testRegisterTransaction_AccountNotFound() {
        Mockito.when(transactionService.registerTransaction(Mockito.anyString(), Mockito.any(TransactionDTO.class)))
                .thenThrow(new AccountNotFoundException("Account with ID " + accountId + " not found"));

        webTestClient.post().uri("/transactions/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"transactionType\": \"WITHDRAW_ATM\", \"amount\": 1000.0, \"description\": \"ATM Withdrawal\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Account with ID " + accountId + " not found");
    }

    @Test
    @DisplayName("Should return a 400 Bad Request Response when transaction is being registered with invalid inputs; " +
            "in this case, missing required fields")
    public void testRegisterTransaction_BadRequest(){
        webTestClient.post().uri("/transactions/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"transactionType\": \"WITHDRAW_ATM\", \"amount\": 1000.0}")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
