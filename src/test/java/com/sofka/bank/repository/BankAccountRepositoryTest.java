package com.sofka.bank.repository;

import com.sofka.bank.entity.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void setUp() {
        bankAccountRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return true when account number exists")
    public void testExistsByAccountNumber_True() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder("John Doe");
        bankAccount.setAccountNumber("1000008");
        bankAccount.setGlobalBalance(1000.0);

        bankAccountRepository.save(bankAccount).block();

        Mono<Boolean> exists = bankAccountRepository.existsByAccountNumber("1000008");

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return false when account number does not exist")
    public void testExistsByAccountNumber_False() {
        Mono<Boolean> exists = bankAccountRepository.existsByAccountNumber("1234567");

        StepVerifier.create(exists)
                .expectNext(false)
                .verifyComplete();
    }
}
