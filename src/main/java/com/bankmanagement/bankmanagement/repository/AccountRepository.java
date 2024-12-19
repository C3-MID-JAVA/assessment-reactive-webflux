package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Flux<Account> findByUserId(String userId);
    Mono<Account> findByAccountNumber(String accountNumber);
}
