package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.TypeTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@DataMongoTest
public class TypeTransactionRepositoryTest {

    private final TypeTransactionRepository repository;

    @Autowired
    public TypeTransactionRepositoryTest(TypeTransactionRepository repository) {
        this.repository = repository;
    }
    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindTypeTransaction() {
        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setType("Deposit from branch");
        typeTransaction.setDescription("Deposit from branch.");
        typeTransaction.setTransactionCost(true);
        typeTransaction.setDiscount(false);
        typeTransaction.setValue(new BigDecimal(1));
        typeTransaction.setStatus("ACTIVE");

        repository.save(typeTransaction).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getType().equals(typeTransaction.getType()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteTypeTransaction() {
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