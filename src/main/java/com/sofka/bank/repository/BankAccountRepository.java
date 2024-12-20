package com.sofka.bank.repository;

import com.sofka.bank.entity.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Mono<Boolean> existsByAccountNumber(String accountNumber);

}