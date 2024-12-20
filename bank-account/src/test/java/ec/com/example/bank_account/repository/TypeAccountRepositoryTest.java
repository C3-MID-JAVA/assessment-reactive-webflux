package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.TypeAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class TypeAccountRepositoryTest {

    private final TypeAccountRepository repository;

    @Autowired
    public TypeAccountRepositoryTest(TypeAccountRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindTypeAccount() {
        TypeAccount typeAccount = new TypeAccount();
        typeAccount.setType("Debit account");
        typeAccount.setDescription("User debit account.");
        typeAccount.setStatus("ACTIVE");

        repository.save(typeAccount).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getType().equals(typeAccount.getType()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteTypeAccount() {
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
