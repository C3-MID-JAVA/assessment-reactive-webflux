package com.reactivo.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import com.reactivo.banco.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionInDTO transactionInDTO;
    private TransactionOutDTO transactionOutDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionInDTO = new TransactionInDTO("Deposit in branch", new BigDecimal("100.00"), "DEPOSIT", LocalDate.now(), "accountId123");
        transactionOutDTO = new TransactionOutDTO("1", "Deposit in branch", new BigDecimal("100.00"), "DEPOSIT", LocalDate.now(), "accountId123");
    }

    @Test
    void shouldMakeBranchDepositSuccessfully() {
        when(transactionService.makeBranchDeposit(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makeBranchDeposit(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldMakeATMDepositSuccessfully() {
        when(transactionService.makeATMDeposit(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makeATMDeposit(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldMakeDepositToAnotherAccountSuccessfully() {
        when(transactionService.makeDepositToAnotherAccount(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makeDepositToAnotherAccount(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldMakePhysicalPurchaseSuccessfully() {
        when(transactionService.makePhysicalPurchase(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makePhysicalPurchase(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldMakeOnlinePurchaseSuccessfully() {
        when(transactionService.makeOnlinePurchase(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makeOnlinePurchase(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldMakeATMWithdrawalSuccessfully() {
        when(transactionService.makeATMWithdrawal(any(TransactionInDTO.class))).thenReturn(Mono.just(transactionOutDTO));

        Mono<ResponseEntity<TransactionOutDTO>> response = transactionController.makeATMWithdrawal(transactionInDTO);

        StepVerifier.create(response)
                .expectNextMatches(res -> {
                    assertEquals(HttpStatus.CREATED, res.getStatusCode());
                    assertEquals("1", res.getBody().getId());
                    return true;
                })
                .verifyComplete();
    }



}
