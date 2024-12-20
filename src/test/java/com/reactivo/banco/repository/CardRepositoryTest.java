package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testSaveAndFindById() {
        Card card = new Card();
        card.setCardNumber("CARD12345");


        Mono<Card> saveMono = cardRepository.save(card);

        StepVerifier.create(saveMono)
                .assertNext(savedCard -> {
                    assertThat(savedCard.getId()).isNotNull();
                    assertThat(savedCard.getCardNumber()).isEqualTo("CARD12345");
                })
                .verifyComplete();

        Mono<Card> findMono = cardRepository.findById(card.getId());

        StepVerifier.create(findMono)
                .assertNext(foundCard -> {
                    assertThat(foundCard.getId()).isEqualTo(card.getId());
                    assertThat(foundCard.getCardNumber()).isEqualTo("CARD12345");
                })
                .verifyComplete();
    }

    @Test
    public void testDelete() {
        Card card = new Card();
        card.setCardNumber("CARD12345");

        Mono<Void> deleteMono = cardRepository.save(card)
                .flatMap(savedCard -> cardRepository.deleteById(savedCard.getId()));

        StepVerifier.create(deleteMono)
                .verifyComplete();

        StepVerifier.create(cardRepository.findById(card.getId()))
                .expectNextCount(0)
                .verifyComplete();
    }

}
