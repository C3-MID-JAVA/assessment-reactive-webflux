package com.sofkau.usrv_accounts_manager.repository;

import com.sofkau.usrv_accounts_manager.model.AccountModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should found an account on the database with the same account number")
    void findByAccountNumber() {
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountNumber("6363636363");
        Mono<AccountModel> accountModelSaved = accountRepository.save(accountModel);
        StepVerifier.create(accountModelSaved)
                .expectNext(accountModel)
                .verifyComplete();

        Mono<AccountModel> accountModelFound = accountRepository.findByAccountNumber(accountModel.getAccountNumber());
        StepVerifier.create(accountModelFound)
                .expectNextMatches(accM -> accM.getAccountNumber().equals("6363636363"))
                .verifyComplete();


    }

    @Test
    @DisplayName("Should NOT found an account the database with the same account number WHEN account not exist")
    void findByAccountNumber_Notfound() {
        Mono<AccountModel> accountModelResponse = accountRepository.findByAccountNumber("9999999999");

        StepVerifier.create(accountModelResponse)
                .expectNextCount(0)
                .verifyComplete();
    }
}