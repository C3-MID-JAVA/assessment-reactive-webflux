package org.bankAccountManager.service.interfaces;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.entity.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransactionService {
    Mono<Transaction> createTransaction(Mono<Transaction> transaction);

    Mono<Transaction> getTransactionById(Mono<Integer> id);

    Flux<Transaction> getAllTransactions();

    Mono<Transaction> getTransactionByBranch(Mono<Branch> branch);

    Flux<Transaction> getTransactionsByDestinationAccount(Mono<Account> destination_account);

    Flux<Transaction> getTransactionsBySourceAccount(Mono<Account> origin_account);

    Flux<Transaction> getTransactionsByDate(Mono<LocalDateTime> date);

    Flux<Transaction> getTransactionsByType(Mono<String> type);

    Mono<Transaction> updateTransaction(Mono<Transaction> transaction);

    Mono<Void> deleteTransaction(Mono<Integer> id);
}
