package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, Integer> {
    Mono<Card> findCardById(int id);

    Mono<Boolean> existsById(int id);

    Mono<Card> findCardByCardNumber(String card_number);

    Flux<Card> findAll();

    Flux<Card> findCardsByCardType(String card_type);

    Mono<Void> deleteById(int id);
}
