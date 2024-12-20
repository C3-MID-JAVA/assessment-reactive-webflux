package org.bankAccountManager.service.interfaces;

import org.bankAccountManager.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> createAccount(Mono<Account> account);

    Mono<Account> getAccountById(Mono<Integer> id);

    Flux<Account> getAccountsByCustomerId(Mono<Integer> customer_id);

    Flux<Account> getAllAccounts();

    Mono<Account> updateAccount(Mono<Account> account);

    Mono<Void> deleteAccount(Mono<Integer> id);
}