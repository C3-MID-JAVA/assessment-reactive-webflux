package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.CardRequestDTO;
import ec.com.example.bank_account.dto.CardResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {

    Mono<CardResponseDTO> createCard(CardRequestDTO card);

    Flux<CardResponseDTO> getAllCards();

}