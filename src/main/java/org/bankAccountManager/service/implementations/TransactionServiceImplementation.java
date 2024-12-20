package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.entity.Transaction;
import org.bankAccountManager.repository.AccountRepository;
import org.bankAccountManager.repository.TransactionRepository;
import org.bankAccountManager.service.interfaces.TransactionService;
import org.bankAccountManager.util.predicate.TransactionValidations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionServiceImplementation implements TransactionService {

    private static final Map<String, BigDecimal> transactionTypes = new HashMap<>();

    static {
        transactionTypes.put("branch_transfer", new BigDecimal("0.00"));
        transactionTypes.put("another_account_deposit", new BigDecimal("1.50"));
        transactionTypes.put("store_card_purchase", new BigDecimal("0.00"));
        transactionTypes.put("online_card_purchase", new BigDecimal("5.00"));
        transactionTypes.put("atm_withdrawal", new BigDecimal("1.00"));
        transactionTypes.put("atm_deposit", new BigDecimal("2.00"));
    }

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public TransactionServiceImplementation(TransactionRepository transactionRepository, AccountRepository accountRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Transaction> createTransaction(Mono<Transaction> transaction) {
        return transaction.flatMap(tEnt ->
                transactionRepository.existsById(tEnt.getId())
                        .flatMap(exists -> {
                            if (exists)
                                return Mono.error(new IllegalArgumentException("Transaction already exists"));
                            if (!TransactionValidations.isValidTransactionType(transactionTypes).test(tEnt.getType()))
                                return Mono.error(new IllegalArgumentException("Invalid transaction type: " + tEnt.getType()));
                            return processTransactionByType(tEnt)
                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")))
                                    .then(reactiveMongoTemplate.save(tEnt));
                        }));
    }

    private Mono<Account> processTransactionByType(Transaction transaction) {
        return executeTransaction(transaction.getDestinationAccount(),
                transaction.getSourceAccount(),
                transaction.getAmount().add(transactionTypes.get(transaction.getType())).negate());
    }

    private Mono<Account> executeTransaction(Account origin, Account destination, BigDecimal amount) {
        if (origin == null || destination == null)
            throw new IllegalArgumentException("Account not found");
        if (amount.compareTo(BigDecimal.ZERO) < 0 && origin.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in origin account: " + origin.getId());
        }
        origin.setBalance(origin.getBalance().add(amount.negate()));
        destination.setBalance(destination.getBalance().add(amount));
        Mono<Account> savedOrigin = accountRepository.save(origin);
        Mono<Account> savedDestination = accountRepository.save(destination);
        return Mono.zip(savedOrigin, savedDestination).then(Mono.just(origin));
    }

    @Override
    public Mono<Transaction> getTransactionById(Mono<Integer> id) {
        return id.flatMap(transactionRepository::findTransactionById);
    }

    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> getTransactionByBranch(Mono<Branch> branch) {
        return branch.flatMap(transactionRepository::findTransactionByBranch);
    }

    @Override
    public Flux<Transaction> getTransactionsByDestinationAccount(Mono<Account> destination_account) {
        return destination_account.flatMapMany(transactionRepository::findTransactionsByDestinationAccount);
    }

    @Override
    public Flux<Transaction> getTransactionsBySourceAccount(Mono<Account> source_account) {
        return source_account.flatMapMany(transactionRepository::findTransactionsBySourceAccount);
    }

    @Override
    public Flux<Transaction> getTransactionsByDate(Mono<LocalDateTime> date) {
        return date.flatMapMany(transactionRepository::findTransactionsByDate);
    }

    @Override
    public Flux<Transaction> getTransactionsByType(Mono<String> type) {
        return type.flatMapMany(transactionRepository::findTransactionsByType);
    }

    @Override
    public Mono<Transaction> updateTransaction(Mono<Transaction> transaction) {
        return transaction.flatMap(tEnt ->
                reactiveMongoTemplate.findAndModify(
                                Query.query(Criteria.where("id").is(tEnt.getId())),
                                new Update()
                                        .set("date", tEnt.getDate())
                                        .set("type", tEnt.getType())
                                        .set("amount", tEnt.getAmount())
                                        .set("description", tEnt.getDescription()),
                                Transaction.class)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found"))));
    }

    @Override
    public Mono<Void> deleteTransaction(Mono<Integer> id) {
        return id.flatMap(transactionRepository::deleteById);
    }
}
