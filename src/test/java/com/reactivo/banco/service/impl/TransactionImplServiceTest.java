package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.entity.Account;
import com.reactivo.banco.model.entity.Transaction;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionImplServiceTest {

//    @Mock
//    private TransactionRepository movimientoRepository;
//
//    @Mock
//    private AccountRepository cuentaRepository;
//
//    private TransactionImplService transactionService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        transactionService = new TransactionImplService(movimientoRepository, cuentaRepository);
//    }
//
//    @Test
//    public void testMakeBranchDeposit_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito en Sucursal", new BigDecimal("500.00"), "crédito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makeBranchDeposit(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("500.00")))
//                .verifyComplete();
//    }
//
//
//    @Test
//    public void testMakeBranchDeposit_InsufficientBalance() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito en Sucursal", new BigDecimal("2000.00"), "crédito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("500.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//
//        StepVerifier.create(transactionService.makeBranchDeposit(transactionInDTO))
//                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("Saldo insuficiente para realizar la transacción."))
//                .verify();
//    }
//
//    @Test
//    public void testMakeATMDeposit_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito en Cajero", new BigDecimal("500.00"), "crédito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makeATMDeposit(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("500.00")))
//                .verifyComplete();
//    }
//
//    @Test
//    public void testMakeDepositToAnotherAccount_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito a Otra Cuenta", new BigDecimal("500.00"), "crédito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makeDepositToAnotherAccount(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("500.00")))
//                .verifyComplete();
//    }
//
//    @Test
//    public void testMakePhysicalPurchase_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Compra Física", new BigDecimal("100.00"), "débito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makePhysicalPurchase(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("100.00")))
//                .verifyComplete();
//    }
//
//    @Test
//    public void testMakeOnlinePurchase_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Compra Web", new BigDecimal("200.00"), "débito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makeOnlinePurchase(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("200.00")))
//                .verifyComplete();
//    }
//
//    @Test
//    public void testMakeATMWithdrawal_Success() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Retiro Cajero", new BigDecimal("300.00"), "débito", LocalDate.now(), "123");
//        Account account = new Account("123", "12345", new BigDecimal("1000.00"), "1", "card123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.just(account));
//        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(account));
//        when(movimientoRepository.save(any(Transaction.class))).thenReturn(Mono.just(new Transaction()));
//
//        StepVerifier.create(transactionService.makeATMWithdrawal(transactionInDTO))
//                .expectNextMatches(transactionOutDTO -> transactionOutDTO.getAmount().equals(new BigDecimal("300.00")))
//                .verifyComplete();
//    }
//
//    @Test
//    public void testMakeTransaction_AccountNotFound() {
//        TransactionInDTO transactionInDTO = new TransactionInDTO("Deposito en Sucursal", new BigDecimal("500.00"), "crédito", LocalDate.now(), "123");
//
//        when(cuentaRepository.findById("123")).thenReturn(Mono.empty());
//
//        StepVerifier.create(transactionService.makeBranchDeposit(transactionInDTO))
//                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("Cuenta no encontrada con ID: 123"))
//                .verify();
//    }
}
