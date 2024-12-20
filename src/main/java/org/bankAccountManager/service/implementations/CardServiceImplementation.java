package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Card;
import org.bankAccountManager.repository.CardRepository;
import org.bankAccountManager.service.interfaces.CardService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImplementation implements CardService {

    private final CardRepository cardRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CardServiceImplementation(CardRepository cardRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.cardRepository = cardRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Card> createCard(Mono<Card> card) {
        return card.flatMap(cEnt ->
                cardRepository.existsById(cEnt.getId()).flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException("Card already exists"));
                    return reactiveMongoTemplate.save(cEnt);
                })
        );
    }

    @Override
    public Mono<Card> getCardById(Mono<Integer> id) {
        return id.flatMap(cardRepository::findCardById);
    }

    @Override
    public Mono<Card> getCardByNumber(Mono<String> card_number) {
        return card_number.flatMap(cardRepository::findCardByCardNumber);
    }

    @Override
    public Flux<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Flux<Card> getCardsByAccount(Mono<Account> account) {
        return account.flatMapMany(aEnt -> Flux.fromIterable(aEnt.getCards())
                .flatMap(card -> cardRepository.findCardById(card.getId())));
    }

    @Override
    public Flux<Card> getCardsByType(Mono<String> card_type) {
        return card_type.flatMapMany(cardRepository::findCardsByCardType);
    }

    @Override
    public Mono<Card> updateCard(Mono<Card> card) {
        return card.flatMap(cEnt ->
                reactiveMongoTemplate.findAndModify(
                                Query.query(Criteria.where("id").is(cEnt.getId())),
                                new Update()
                                        .set("cardNumber", cEnt.getCardNumber())
                                        .set("cardType", cEnt.getCardType())
                                        .set("expirationDate", cEnt.getExpirationDate())
                                        .set("cvv", cEnt.getCvv()),
                                Card.class)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Card not found"))));
    }

    @Override
    public Mono<Void> deleteCard(Mono<Integer> id) {
        return id.flatMap(cardRepository::deleteById);
    }
}
