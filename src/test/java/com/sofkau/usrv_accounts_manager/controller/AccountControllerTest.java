package com.sofkau.usrv_accounts_manager.controller;


import com.sofkau.usrv_accounts_manager.Utils.ErrorDetails;
import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AccountControllerTest {

    @Autowired
    private WebTestClient webTestclient;

    @MockitoBean
    private AccountService service;

    @MockitoBean
    private AccountRepository accountRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should create a new account and return the complete body response")
    void createAccount() {

        AccountDTO accountDTORequest = new AccountDTO(null,"12345",
                BigDecimal.valueOf(100), "DEBIT", "My cas", null);
        AccountDTO accountDTOResponse = new AccountDTO(new ArrayList<>(),"12345",
                BigDecimal.valueOf(100), "DEBIT", "My cas", new ArrayList<>());

        //when(accountRepository.findByAccountNumber(any(String.class))).thenReturn(Mono.just(new AccountModel()));
        when(service.createAccount(any(AccountDTO.class))).thenReturn(Mono.just(accountDTOResponse));

        webTestclient
                .post()
                .uri("/api/v1/account/create")
                        .bodyValue(accountDTORequest)
                .exchange()
                        .expectStatus().isOk()
                .expectBody(AccountDTO.class)
                .consumeWith(response -> {
                    AccountDTO actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(accountDTOResponse.getAccountNumber(), actualResponse.getAccountNumber());
                });


        verify(service, times(1)).createAccount(any(AccountDTO.class));

    }

    @Test
    @DisplayName("Should return an error about a null field")
    void createAccount_throws_Error() {

        AccountDTO accountDTORequest = new AccountDTO(new ArrayList<>(),"12345",
                BigDecimal.valueOf(100), "DEBIT", null, new ArrayList<>());
        AccountDTO accountDTOResponse = new AccountDTO(new ArrayList<>(),"12345",
                BigDecimal.valueOf(100), "DEBIT", "My cas", new ArrayList<>());

        when(service.createAccount(any(AccountDTO.class))).thenReturn(Mono.just(accountDTOResponse));

        webTestclient
                .post()
                .uri("/api/v1/account/create")
                .bodyValue(accountDTORequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class)
                .consumeWith(response -> {
                    ErrorDetails actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals("SOME FIELD(s) IN THE REQUEST HAS ERROR", actualResponse.getMessage());
                    assertTrue(String.valueOf(actualResponse.getDetails()).contains("accountOwner: accountOwner cannot be empty"));
                });

        verify(service, times(0)).createAccount(any(AccountDTO.class));

    }

    @Test
    @DisplayName("Should  retrieve all accounts")
    void shouldReturnOkWhenAccountsExist() {

        AccountDTO accountDTO1 = new AccountDTO(new ArrayList<>(),"12345",
                BigDecimal.valueOf(100), "DEBIT", null, new ArrayList<>());
        AccountDTO accountDTO2 = new AccountDTO(new ArrayList<>(),"12345",
                BigDecimal.valueOf(100), "DEBIT", null, new ArrayList<>());

        when(service.getAllAccounts())
                .thenReturn(Flux.just(accountDTO1, accountDTO2));

        webTestclient.get()
                .uri("/api/v1/account")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountDTO.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<AccountDTO> actualResponse = response.getResponseBody();
                    assert actualResponse != null;
                    assertEquals(accountDTO1.getAccountNumber(), actualResponse.get(0).getAccountNumber());
                    assertEquals(accountDTO2.getAccountNumber(), actualResponse.get(1).getAccountNumber());
                });

        verify(service, times(1)).getAllAccounts();

    }
}