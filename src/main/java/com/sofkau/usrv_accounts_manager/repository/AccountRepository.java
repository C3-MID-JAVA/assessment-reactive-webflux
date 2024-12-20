package com.sofkau.usrv_accounts_manager.repository;

import com.sofkau.usrv_accounts_manager.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends ReactiveMongoRepository<AccountModel, String> {
    Mono<AccountModel> findByAccountNumber(String accountNumber);

}
