package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

@DataMongoTest
public class CardRepositoryTest {

    private final CardRepository repository;

    @Autowired
    public CardRepositoryTest(CardRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindCard() {
        Card card = new Card();
        card.setHolderName("Diego Loor");
        card.setLimitation(new BigDecimal(1000));
        card.setCvcCode("234");
        card.setStatus("Loor");
        card.setExpirationDate(new Date());
        card.setStatus("ACTIVE");

        repository.save(card).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getHolderName().equals(card.getHolderName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteCard() {
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