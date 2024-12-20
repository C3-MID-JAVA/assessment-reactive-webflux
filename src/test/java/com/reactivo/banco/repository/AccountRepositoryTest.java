package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class AccountRepositoryTest {

    private AccountRepository accountRepository;


    @Autowired
    public AccountRepositoryTest(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    public void testSaveAndFindById() {
        Account account = new Account();
        account.setAccountNumber("123456");
        account.setBalance(new BigDecimal(500.0));
        account.setCustumerId("CUST001");
        account.setCardId("CARD001");

        Mono<Account> saveMono = accountRepository.save(account);

        StepVerifier.create(saveMono)
                .assertNext(savedAccount -> {
                    assertThat(savedAccount.getId()).isNotNull();
                    assertThat(savedAccount.getAccountNumber()).isEqualTo("123456");
                })
                .verifyComplete();

        Mono<Account> findMono = accountRepository.findById(account.getId());

        StepVerifier.create(findMono)
                .assertNext(foundAccount -> {
                    assertThat(foundAccount.getId()).isEqualTo(account.getId());
                    assertThat(foundAccount.getAccountNumber()).isEqualTo("123456");
                    assertThat(foundAccount.getBalance()).isEqualByComparingTo(new BigDecimal("500.00"));
                    assertThat(foundAccount.getCustumerId()).isEqualTo("CUST001");
                })
                .verifyComplete();
    }

    @Test
    public void testDelete() {
        Account account = new Account();
        account.setAccountNumber("123456");
        account.setBalance(new BigDecimal(500.0));
        account.setCustumerId("CUST001");
        account.setCardId("CARD001");

        Mono<Void> deleteMono = accountRepository.save(account)
                .flatMap(savedAccount -> accountRepository.deleteById(savedAccount.getId()));

        StepVerifier.create(deleteMono)
                .verifyComplete();

        StepVerifier.create(accountRepository.findById(account.getId()))
                .expectNextCount(0)
                .verifyComplete();
    }
}
