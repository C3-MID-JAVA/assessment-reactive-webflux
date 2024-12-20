package org.bankAccountManager.service.interfaces;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<Card> createCard(Mono<Card> card);

    Mono<Card> getCardById(Mono<Integer> id);

    Mono<Card> getCardByNumber(Mono<String> card_number);

    Flux<Card> getAllCards();

    Flux<Card> getCardsByAccount(Mono<Account> account);

    Flux<Card> getCardsByType(Mono<String> card_type);

    Mono<Card> updateCard(Mono<Card> card);

    Mono<Void> deleteCard(Mono<Integer> id);
}