package com.example.bank.repository;

import com.example.bank.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class AccountRepositoryTests {
    @Autowired
    AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("123", "holder", BigDecimal.ZERO, new ArrayList<>());
    }

    @Test
    public void testSaveAccount() {
        Mono<Account> savedAccount = accountRepository.save(account);
        assertEquals(account.getId(), savedAccount.block().getId());
    }

    @Test
    public void testFindAllAccounts() {
        Flux<Account> accounts = accountRepository.findAll();
        assertNotNull(accounts);
    }

    @Test
    public void testFindAccountById() {
        Mono<Account> savedAccount = accountRepository.save(account);
        Mono<Account> foundAccount = accountRepository.findById(account.getId());
        assertNotNull(foundAccount);
        assertEquals(savedAccount.block().getId(), foundAccount.block().getId());
    }
}
