package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findAllByAccountId(String accountId);
}
