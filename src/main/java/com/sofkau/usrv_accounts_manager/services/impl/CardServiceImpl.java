package com.sofkau.usrv_accounts_manager.services.impl;


import com.sofkau.usrv_accounts_manager.Utils.Utils;
import com.sofkau.usrv_accounts_manager.dto.CardDTO;
import com.sofkau.usrv_accounts_manager.mapper.DTOMapper;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.repository.CardRepository;
import com.sofkau.usrv_accounts_manager.services.CardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardServiceImpl(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    private Mono<String> generateUniqueCvv() {

        Supplier<String> cvvGenerator = Utils::generateCvvCode;

        return Mono.defer(() -> Mono.just(cvvGenerator.get()))
                .flatMap(cvv -> existsCvv(cvv)
                        .flatMap(exists -> exists ? Mono.empty() : Mono.just(cvv)))
                .repeat()
                .next();
    }


    @Override
    public Mono<Boolean> existsCvv(String cvv) {
        return cardRepository.existsByCardCVV(cvv);
    }

    @Transactional
    @Override
    public Mono<CardDTO> createCard(CardDTO cardDTO) {
        return cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .hasElement()
                .flatMap(existCard -> {
                    if (existCard) {
                        return Mono.error(new RuntimeException("Card already exists"));
                    }
                    return accountRepository.findByAccountNumber(cardDTO.getAccount().getAccountNumber())
                            .flatMap(account -> {
                                CardModel cardModel = DTOMapper.toCardModel(cardDTO);
                                return generateUniqueCvv()
                                        .flatMap(cvv -> {
                                            cardModel.setCardCVV(cvv);
                                            cardModel.setAccount(account);
                                            return cardRepository.save(cardModel).map(DTOMapper::toCardDTO);
                                        });
                            })
                            .switchIfEmpty(Mono.error(new RuntimeException("Account does not exist")));
                });
    }

    @Override
    public Flux<CardDTO> getCardsByAccount(String accountId) {
        return accountRepository.findByAccountNumber(accountId)
                .flatMapMany(accountModel -> cardRepository.findByAccount_Id(accountModel.getId()))
                .map(DTOMapper::toCardDTO);
    }


}
