package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.Account;
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

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account1;
    private Account account2;

    @BeforeAll
    void setup() {
        account1 = Account.builder()
                .id("1")
                .accountNumber("ACC123")
                .balance(500.0)
                .userId("USER1")
                .build();

        account2 = Account.builder()
                .id("2")
                .accountNumber("ACC456")
                .balance(1000.0)
                .userId("USER2")
                .build();
    }

    @BeforeEach
    void init() {
        accountRepository.deleteAll().block();
        accountRepository.saveAll(Flux.just(account1, account2)).blockLast();
    }

    @Test
    void findById_shouldReturnAccount_whenAccountExists() {
        StepVerifier.create(accountRepository.findById("1"))
                .expectNextMatches(account -> account.getAccountNumber().equals("ACC123")
                        && account.getBalance() == 500.0
                        && account.getUserId().equals("USER1"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenAccountDoesNotExist() {
        StepVerifier.create(accountRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnAccounts_whenUserHasAccounts() {
        StepVerifier.create(accountRepository.findByUserId("USER1"))
                .expectNextMatches(account -> account.getAccountNumber().equals("ACC123"))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenUserHasNoAccounts() {
        StepVerifier.create(accountRepository.findByUserId("NON_EXISTENT_USER"))
                .verifyComplete();
    }

    @Test
    void findByAccountNumber_shouldReturnAccount_whenAccountNumberExists() {
        StepVerifier.create(accountRepository.findByAccountNumber("ACC456"))
                .expectNextMatches(account -> account.getUserId().equals("USER2"))
                .verifyComplete();
    }

    @Test
    void findByAccountNumber_shouldReturnEmpty_whenAccountNumberDoesNotExist() {
        StepVerifier.create(accountRepository.findByAccountNumber("INVALID_ACC"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistAccount() {
        Account newAccount = Account.builder()
                .id("3")
                .accountNumber("ACC789")
                .balance(300.0)
                .userId("USER3")
                .build();

        StepVerifier.create(accountRepository.save(newAccount))
                .expectNextMatches(account -> account.getId().equals("3")
                        && account.getAccountNumber().equals("ACC789"))
                .verifyComplete();

        StepVerifier.create(accountRepository.findById("3"))
                .expectNextMatches(account -> account.getBalance() == 300.0 && account.getUserId().equals("USER3"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveAccount() {
        StepVerifier.create(accountRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(accountRepository.findById("1"))
                .verifyComplete();
    }
}
