package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

    Mono<Account> findAccountById(int id);

    Mono<Boolean> existsById(int id);

    Flux<Account> findAll();

    Mono<Account> getAccountByTransactions(List<Transaction> transactions);

    Mono<Void> deleteById(int id);
}
