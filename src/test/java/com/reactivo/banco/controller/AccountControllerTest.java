package com.reactivo.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.AccountInDTO;
import com.reactivo.banco.model.dto.AccountOutDTO;
import com.reactivo.banco.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;


public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountInDTO accountInDTO;
    private AccountOutDTO accountOutDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountInDTO = new AccountInDTO("12345", new BigDecimal("1000.00"), "customerId", "cardId");
        accountOutDTO = new AccountOutDTO("1", "12345", new BigDecimal("1000.00"), "customerId", "cardId");
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        when(accountService.createAccount(any(AccountInDTO.class))).thenReturn(Mono.just(accountOutDTO));

        Mono<ResponseEntity<AccountOutDTO>> response = accountController.createAccount(accountInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    assertEquals("12345", res.getBody().getAccountNumber());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnAllAccountsSuccessfully() {
        when(accountService.getAllAccounts()).thenReturn(Flux.just(accountOutDTO));

        Mono<ResponseEntity<Flux<AccountOutDTO>>> response = accountController.getAllAccounts();

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertTrue(res.getBody().collectList().block().size() > 0);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnAccountByIdSuccessfully() {
        when(accountService.getAccountById(anyString())).thenReturn(Mono.just(accountOutDTO));

        Mono<ResponseEntity<AccountOutDTO>> response = accountController.getAccountById("1");

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() {
        when(accountService.getAccountById(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<AccountOutDTO>> response = accountController.getAccountById("non-existent-id");

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        when(accountService.updateAccount(anyString(), any(AccountInDTO.class))).thenReturn(Mono.just(accountOutDTO));

        Mono<ResponseEntity<AccountOutDTO>> response = accountController.updateAccount("1", accountInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.OK, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentAccount() {
        when(accountService.updateAccount(anyString(), any(AccountInDTO.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<AccountOutDTO>> response = accountController.updateAccount("non-existent-id", accountInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldDeleteAccountSuccessfully() {
        when(accountService.deleteAccount(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = accountController.deleteAccount("1");

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
                    return true;
                })
                .verifyComplete();
    }

}
