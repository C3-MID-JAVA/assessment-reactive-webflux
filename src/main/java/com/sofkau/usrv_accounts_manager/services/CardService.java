package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CardService {
    Mono<Boolean> existsCvv(String cvv);
    Mono<CardDTO> createCard(CardDTO cardDTO);
    Flux<CardDTO> getCardsByAccount(String accountId);
}
