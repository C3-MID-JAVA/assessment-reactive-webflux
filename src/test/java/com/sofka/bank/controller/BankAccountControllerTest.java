package com.sofka.bank.controller;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BankAccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BankAccountService bankAccountService;


    private BankAccountDTO validAccountDTO;

    @BeforeEach
    public void setup() {
        validAccountDTO = new BankAccountDTO(null, "1000008", "John Doe", 5000.0, new ArrayList<>());
    }

    @Test
    @DisplayName("Should retrieve all accounts and return 200 OK")
    public void testGetAllAccounts_Success() {
        Flux<BankAccountDTO> accounts = Flux.just(validAccountDTO);
        Mockito.when(bankAccountService.getAllAccounts()).thenReturn(accounts);

        webTestClient.get()
                .uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BankAccountDTO.class).isEqualTo(Arrays.asList(validAccountDTO));

    }

    @Test
    @DisplayName("Should return an empty list with a 204 no content response when no accounts are found")
    public void testGetAllAccounts_NoContent() {
        Mockito.when(bankAccountService.getAllAccounts()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/accounts")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Should return a successfully created account with 201 CREATED")
    public void testCreateAccount_Success() {
        Mockito.when(bankAccountService.createAccount(Mockito.any(BankAccountDTO.class)))
                .thenReturn(Mono.just(validAccountDTO));

        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"accountHolder\": \"John Doe\", \"accountNumber\": \"1000008\", \"globalBalance\": 5000.0}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BankAccountDTO.class).isEqualTo(validAccountDTO);
    }

    @Test
    @DisplayName("Should return a 400 Bad Request response when trying to create account with invalid request body; " +
            "in this case, a missing accountHolder field")
    public void testCreateAccount_BadRequest(){
        webTestClient.post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"accountNumber\": \"1000008\", \"globalBalance\": 5000.0}")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
