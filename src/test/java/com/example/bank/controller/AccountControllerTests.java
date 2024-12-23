package com.example.bank.controller;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import com.example.bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountRequestDTO accountRequest;
    private AccountResponseDTO accountResponse;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(accountController).build();
        accountRequest = new AccountRequestDTO("holder", BigDecimal.ZERO);
        accountResponse = new AccountResponseDTO("123", "holder", BigDecimal.ZERO, new ArrayList<>());
    }

    @Test
    void createAccount_ReturnsCreated() throws Exception {
        when(accountService.createAccount(any(AccountRequestDTO.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/accounts")
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123");

        verify(accountService, times(1)).createAccount(any(AccountRequestDTO.class));
    }

    @Test
    void createAccount_ReturnsBadRequest() throws Exception {
        /*when(accountService.createAccount(any(AccountRequestDTO.class)))
                .thenThrow(new BadRequestException("Request body not valid"));

        webTestClient.post()
                .uri("/accounts")
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(accountService, times(1)).createAccount(any(AccountRequestDTO.class));*/
    }

    @Test
    void getAllAccounts_ReturnsAccountsList() throws Exception {
        List<AccountResponseDTO> accountList = List.of(accountResponse);

        when(accountService.getAllAccounts()).thenReturn(Flux.fromIterable(accountList));

        webTestClient.get()
                .uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("123");

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void getAllAccounts_ReturnsAccountsEmpty() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(Flux.fromIterable(new ArrayList<>()));

        webTestClient.get()
                .uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void getById_ReturnsAccount() throws Exception {
        when(accountService.getAccountById("123")).thenReturn(Mono.just(accountResponse));

        webTestClient.get()
                .uri("/accounts/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123");

        verify(accountService, times(1)).getAccountById("123");
    }

    @Test
    void getById_ReturnsEmpty() throws Exception {
        when(accountService.getAccountById("123")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/accounts/123")
                .exchange()
                .expectStatus().isOk().expectBody().isEmpty();

        verify(accountService, times(1)).getAccountById("123");
    }
}
