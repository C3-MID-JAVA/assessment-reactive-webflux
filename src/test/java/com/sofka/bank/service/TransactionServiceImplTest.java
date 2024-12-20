package com.sofka.bank.service;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.dto.TransactionDTO;
import com.sofka.bank.entity.BankAccount;
import com.sofka.bank.entity.Transaction;
import com.sofka.bank.entity.TransactionType;
import com.sofka.bank.exceptions.AccountNotFoundException;
import com.sofka.bank.exceptions.InsufficientFundsException;
import com.sofka.bank.repository.BankAccountRepository;
import com.sofka.bank.repository.TransactionRepository;
import com.sofka.bank.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private BankAccount account;
    private TransactionDTO validTransactionDTO;
    private String accountId;

    @BeforeEach
    public void setup() {
        accountId = "12345";
        account = new BankAccount(accountId, "1000008", "John Doe", 5000.0, new ArrayList<>());

        validTransactionDTO = new TransactionDTO("40", TransactionType.WITHDRAW_ATM, 1000, 0, LocalDateTime.now(),
                "ATM Withdrawal", new BankAccountDTO(accountId, "1000008", "John Doe", 5000.0, new ArrayList<>()));

        Mockito.when(bankAccountRepository.findById(accountId)).thenReturn(Mono.just(account));
    }

    @Test
    @DisplayName("Should perform successful transaction and subtract the correct amount and fee")
    public void testRegisterTransaction_Success() {
        double fee = validTransactionDTO.getTransactionType().getFee();
        double newBalance = account.getGlobalBalance() - validTransactionDTO.getAmount() - fee;

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(Mono.just(new Transaction(validTransactionDTO.getId(), validTransactionDTO.getTransactionType(),
                        validTransactionDTO.getAmount(), fee, LocalDateTime.now(), validTransactionDTO.getDescription(), account)));

        Mockito.when(bankAccountRepository.save(Mockito.any(BankAccount.class)))
                .thenReturn(Mono.just(account));

        // Usamos StepVerifier para esperar y verificar el resultado del Mono
        StepVerifier.create(transactionService.registerTransaction(accountId, validTransactionDTO))
                .expectNextMatches(result -> {
                    assertEquals("ATM Withdrawal", result.getDescription());
                    assertEquals(newBalance, account.getGlobalBalance(), 0.01);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return an error message when amount exceeds balance")
    public void testRegisterTransaction_InsufficientFunds() {
        validTransactionDTO.setAmount(6000.0);

        StepVerifier.create(transactionService.registerTransaction(accountId, validTransactionDTO))
                .expectErrorMatches(throwable -> throwable instanceof InsufficientFundsException
                        && throwable.getMessage().equals("Insufficient balance for transaction"))
                .verify();
    }

    @Test
    @DisplayName("Should return an error message when the account for which transaction is intended is not found")
    public void testRegisterTransaction_AccountNotFound() {
        Mockito.when(bankAccountRepository.findById(accountId)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.registerTransaction(accountId, validTransactionDTO))
                .expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
                        && throwable.getMessage().equals("Account with ID " + accountId + " not found"))
                .verify();
    }

    @Test
    @DisplayName("Should successfully retrieve correct balance for specific account")
    public void testGetGlobalBalance_Success() {
        Mockito.when(bankAccountRepository.findById(accountId)).thenReturn(Mono.just(account));

        StepVerifier.create(transactionService.getGlobalBalance(accountId))
                .expectNext(5000.0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error message when the account for which the balance was consulted is not found")
    public void testGetGlobalBalance_AccountNotFound() {
        Mockito.when(bankAccountRepository.findById(accountId)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.getGlobalBalance(accountId))
                .expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
                        && throwable.getMessage().equals("Account with ID " + accountId + " not found"))
                .verify();
    }
}
