package ec.com.example.bank_account.service.impl;

import ec.com.example.bank_account.dto.CardRequestDTO;
import ec.com.example.bank_account.dto.CardResponseDTO;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.CardMapper;
import ec.com.example.bank_account.repository.CardRepository;
import ec.com.example.bank_account.service.CardService;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public Mono<CardResponseDTO> createCard(CardRequestDTO card) {
        return Mono.just(card)
                .map(cardMapper::mapToEntity)
                .flatMap(cardRepository::save)
                .map(cardMapper::mapToDTO);
    }

    @Override
    public Flux<CardResponseDTO> getAllCards() {
        return cardRepository.findAll()
                .map(cardMapper::mapToDTO)
                .switchIfEmpty(Flux.error(new EmptyCollectionException("No cards records found.")));
    }
}