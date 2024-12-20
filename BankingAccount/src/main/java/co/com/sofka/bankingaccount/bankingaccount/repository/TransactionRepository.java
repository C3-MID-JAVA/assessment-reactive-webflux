package co.com.sofka.bankingaccount.bankingaccount.repository;

import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<TransactionBase, String> {
    Flux<TransactionBase> findByAccountId(String accountId);
}
