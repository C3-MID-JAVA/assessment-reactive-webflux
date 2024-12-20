package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@DataMongoTest
public class AccountRepositoryTest {

    private final AccountRepository repository;

    @Autowired
    public AccountRepositoryTest(AccountRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindAccount() {
        Account account = new Account();
        account.setNumber("2200000000");
        account.setAvailableBalance(new BigDecimal(100));
        account.setRetainedBalance(new BigDecimal(0));
        account.setStatus("ACTIVE");

        repository.save(account).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getNumber().equals(account.getNumber()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteAccount() {
        Mono<Void> deleteResult = repository.deleteAll();

        StepVerifier.create(deleteResult)
                .expectComplete()
                .verify();

        StepVerifier.create(repository.findAll())
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}