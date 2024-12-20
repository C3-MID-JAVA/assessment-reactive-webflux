package com.sofkau.usrv_accounts_manager.repository;


import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends ReactiveMongoRepository<CardModel, String> {
    Mono<Boolean> existsByCardCVV(String cardCVV);
    Mono<CardModel> findByCardNumber(String cardNumber);

    Flux<CardModel> findByAccount_Id(String accountId);

}
