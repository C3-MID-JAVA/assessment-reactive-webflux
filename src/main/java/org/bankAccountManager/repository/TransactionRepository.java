package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.entity.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, Long> {
    Mono<Transaction> findTransactionById(int id);

    Mono<Boolean> existsById(int id);

    Flux<Transaction> findAll();

    Flux<Transaction> findTransactionsByDestinationAccount(Account destination_account);

    Flux<Transaction> findTransactionsBySourceAccount(Account source_account);

    Mono<Transaction> findTransactionByBranch(Branch branch);

    Flux<Transaction> findTransactionsByDate(LocalDateTime date);

    Flux<Transaction> findTransactionsByType(String type);

    Mono<Void> deleteById(int id);
}
