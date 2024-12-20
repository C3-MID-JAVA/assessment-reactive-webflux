package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

@DataMongoTest
public class TransactionRepositoryTest {

    private final TransactionRepository repository;

    @Autowired
    public TransactionRepositoryTest(TransactionRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDetails("transaction made in Manabí.");
        transaction.setDate(new Date());
        transaction.setValue(new BigDecimal(100));
        transaction.setStatus("ACTIVE");
        transaction.setAccount(null);
        transaction.setTypeTransaction(null);

        repository.save(transaction).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getDate().equals(transaction.getDate()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteTransaction() {
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