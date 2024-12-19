package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.Transaction;
import com.bankmanagement.bankmanagement.model.TransactionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeAll
    void setup() {
        transaction1 = Transaction.builder()
                .id("1")
                .amount(100.0)
                .fee(2.0)
                .netAmount(98.0)
                .type(TransactionType.ATM_DEPOSIT)
                .timestamp(LocalDateTime.now())
                .accountId("ACC123")
                .build();

        transaction2 = Transaction.builder()
                .id("2")
                .amount(50.0)
                .fee(1.0)
                .netAmount(49.0)
                .type(TransactionType.ATM_WITHDRAWAL)
                .timestamp(LocalDateTime.now())
                .accountId("ACC123")
                .build();
    }

    @BeforeEach
    void init() {
        transactionRepository.deleteAll().block();
        transactionRepository.saveAll(Flux.just(transaction1, transaction2)).blockLast();
    }

    @Test
    void findById_shouldReturnTransaction_whenTransactionExists() {
        StepVerifier.create(transactionRepository.findById("1"))
                .expectNextMatches(transaction -> transaction.getAmount() == 100.0
                        && transaction.getFee() == 2.0
                        && transaction.getType() == TransactionType.ATM_DEPOSIT
                        && transaction.getAccountId().equals("ACC123"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenTransactionDoesNotExist() {
        StepVerifier.create(transactionRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void findAllByAccountId_shouldReturnTransactions_whenAccountHasTransactions() {
        StepVerifier.create(transactionRepository.findAllByAccountId("ACC123"))
                .expectNextMatches(transaction -> transaction.getId().equals("1"))
                .expectNextMatches(transaction -> transaction.getId().equals("2"))
                .verifyComplete();
    }

    @Test
    void findAllByAccountId_shouldReturnEmpty_whenAccountHasNoTransactions() {
        StepVerifier.create(transactionRepository.findAllByAccountId("NON_EXISTENT_ACC"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistTransaction() {
        Transaction newTransaction = Transaction.builder()
                .id("3")
                .amount(200.0)
                .fee(5.0)
                .netAmount(195.0)
                .type(TransactionType.ATM_DEPOSIT)
                .timestamp(LocalDateTime.now())
                .accountId("ACC789")
                .build();

        StepVerifier.create(transactionRepository.save(newTransaction))
                .expectNextMatches(transaction -> transaction.getId().equals("3")
                        && transaction.getAmount() == 200.0
                        && transaction.getNetAmount() == 195.0)
                .verifyComplete();

        StepVerifier.create(transactionRepository.findById("3"))
                .expectNextMatches(transaction -> transaction.getType() == TransactionType.ATM_DEPOSIT
                        && transaction.getAccountId().equals("ACC789"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveTransaction() {
        StepVerifier.create(transactionRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(transactionRepository.findById("1"))
                .verifyComplete();
    }
}
