package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import com.reactivo.banco.model.entity.Account;
import com.reactivo.banco.model.entity.Transaction;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.TransactionRepository;
import com.reactivo.banco.util.TransactionCost;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TransactionImplServiceTest {

    @Mock
    private TransactionRepository movimientoRepository;

    @Mock
    private AccountRepository cuentaRepository;

    @InjectMocks
    private TransactionImplService transactionService;

    private TransactionInDTO transactionInDTO;
    private Account mockAccount;

    @BeforeEach
    public void setUp() {
        transactionInDTO = new TransactionInDTO("Deposit", new BigDecimal("1000.00"), "DEPOSIT", LocalDate.now(), "12345");

        mockAccount = new Account();
        mockAccount.setId("12345");
        mockAccount.setBalance(new BigDecimal("5000.00"));
    }



    @Test
    public void testMakeATMDeposit_AccountNotFound() {
        when(cuentaRepository.findById(transactionInDTO.getAccountId())).thenReturn(Mono.empty());

        Mono<TransactionOutDTO> result = transactionService.makeATMDeposit(transactionInDTO);

        result.subscribe(
                transactionOutDTO -> fail("Expected ResourceNotFoundException to be thrown"),
                throwable -> assertTrue(throwable instanceof ResourceNotFoundException)
        );

        verify(cuentaRepository).findById(transactionInDTO.getAccountId());
        verify(movimientoRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testMakeATMWithdrawal_InsufficientBalance() {
        mockAccount.setBalance(new BigDecimal("50.00"));

        when(cuentaRepository.findById(transactionInDTO.getAccountId())).thenReturn(Mono.just(mockAccount));

        Mono<TransactionOutDTO> result = transactionService.makeATMWithdrawal(transactionInDTO);

        result.subscribe(
                transactionOutDTO -> fail("Expected ResourceNotFoundException to be thrown"),
                throwable -> assertTrue(throwable instanceof ResourceNotFoundException)
        );

        verify(cuentaRepository).findById(transactionInDTO.getAccountId());
        verify(movimientoRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testMakeTransaction_AccountNotFound() {
        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito en Sucursal", new BigDecimal("500.00"), "crédito", LocalDate.now(), "123");

        when(cuentaRepository.findById("123")).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.makeBranchDeposit(transactionInDTO))
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("Cuenta no encontrada con ID: 123"))
                .verify();
    }

    @Test
    public void testMakeDepositToAnotherAccount() {
        when(cuentaRepository.findById(transactionInDTO.getAccountId())).thenReturn(Mono.just(mockAccount));
        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(mockAccount));

        Mono<TransactionOutDTO> result = transactionService.makeDepositToAnotherAccount(transactionInDTO);

        result.subscribe(transactionOutDTO -> {
            assertNotNull(transactionOutDTO);
            assertEquals(transactionInDTO.getAmount().subtract(new BigDecimal(1.5)), transactionOutDTO.getAmount());
        });

        verify(cuentaRepository).findById(transactionInDTO.getAccountId());
        verify(cuentaRepository).save(any(Account.class));
        verify(movimientoRepository).save(any(Transaction.class));
    }

    @Test
    public void testMakePhysicalPurchase() {
        when(cuentaRepository.findById(transactionInDTO.getAccountId())).thenReturn(Mono.just(mockAccount));
        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(mockAccount));

        Mono<TransactionOutDTO> result = transactionService.makePhysicalPurchase(transactionInDTO);

        result.subscribe(transactionOutDTO -> {
            assertNotNull(transactionOutDTO);
            assertEquals(transactionInDTO.getAmount(), transactionOutDTO.getAmount());
        });

        verify(cuentaRepository).findById(transactionInDTO.getAccountId());
        verify(cuentaRepository).save(any(Account.class));
        verify(movimientoRepository).save(any(Transaction.class));
    }

    @Test
    public void testMakeOnlinePurchase() {
        when(cuentaRepository.findById(transactionInDTO.getAccountId())).thenReturn(Mono.just(mockAccount));
        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(mockAccount));

        Mono<TransactionOutDTO> result = transactionService.makeOnlinePurchase(transactionInDTO);

        result.subscribe(transactionOutDTO -> {
            assertNotNull(transactionOutDTO);
            assertEquals(transactionInDTO.getAmount().subtract(new BigDecimal(5.0)), transactionOutDTO.getAmount());
        });

        verify(cuentaRepository).findById(transactionInDTO.getAccountId());
        verify(cuentaRepository).save(any(Account.class));
        verify(movimientoRepository).save(any(Transaction.class));
    }
}
