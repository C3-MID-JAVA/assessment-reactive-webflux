package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.repository.AccountRepository;
import org.bankAccountManager.repository.CustomerRepository;
import org.bankAccountManager.service.interfaces.AccountService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public AccountServiceImplementation(AccountRepository accountRepository, CustomerRepository customerRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Account> createAccount(Mono<Account> account) {
        return account.flatMap(aEnt ->
                accountRepository.existsById(aEnt.getId()).flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException("Account already exists"));
                    return reactiveMongoTemplate.save(aEnt);
                })
        );
    }

    //tabnine
    @Override
    public Mono<Account> getAccountById(Mono<Integer> id) {
        return id.flatMap(accountRepository::findAccountById);
    }

    @Override
    public Flux<Account> getAccountsByCustomerId(Mono<Integer> customer_id) {
        return customer_id.flatMapMany(customerRepository::findCustomerById)
                .flatMap(customer -> Flux.fromIterable(customer.getAccounts()));
    }

    @Override
    public Flux<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Mono<Account> updateAccount(Mono<Account> account) {
        return account.flatMap(aEnt ->
                reactiveMongoTemplate.findAndModify(
                                Query.query(Criteria.where("id").is(aEnt.getId())),
                                new Update()
                                        .set("accountNumber", aEnt.getAccountNumber())
                                        .set("accountType", aEnt.getAccountType())
                                        .set("balance", aEnt.getBalance()),
                                Account.class)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found"))));
    }

    @Override
    public Mono<Void> deleteAccount(Mono<Integer> id) {
        return id.flatMap(accountRepository::deleteById);
    }
}
