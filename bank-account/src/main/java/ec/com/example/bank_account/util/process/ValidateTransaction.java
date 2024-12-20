package ec.com.example.bank_account.util.process;

import ec.com.example.bank_account.entity.Transaction;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ValidateTransaction {
    Mono<Transaction> validateTransaction(Transaction transaction);
}
