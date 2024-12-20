package ec.com.example.bank_account.util.process;

import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.exception.RecordNotFoundException;
import ec.com.example.bank_account.exception.TransactionRejectedException;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.repository.TypeTransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
public class ValidateTransactionImp implements ValidateTransaction {

    private final AccountRepository accountRepository;
    private final TypeTransactionRepository typeTransactionRepository;

    public ValidateTransactionImp(AccountRepository accountRepository, TypeTransactionRepository typeTransactionRepository) {
        this.accountRepository = accountRepository;
        this.typeTransactionRepository = typeTransactionRepository;
    }

    public Mono<Transaction> validateTransaction(Transaction transaction) {
        return Mono.zip(
                accountRepository.findByNumber(transaction.getAccountNumber())
                        .switchIfEmpty(Mono.error(new RecordNotFoundException("Account not found."))),
                typeTransactionRepository.findById(transaction.getTypeTransactionIdentify())
                        .switchIfEmpty(Mono.error(new RecordNotFoundException("TypeTransaction not found.")))
        ).map(tuple -> {
            transaction.setAccount(tuple.getT1());
            transaction.setTypeTransaction(tuple.getT2());
            return transaction;
        }).flatMap(this::validateTransactionRules);
    }

    private Mono<Transaction> validateTransactionRules(Transaction transaction) {
        Predicate<Transaction> isAccountActive = txn ->
                "ACTIVE".equals(txn.getAccount().getStatus());

        Predicate<Transaction> isAccountRejected = txn ->
                txn.getTypeTransaction().getDiscount() &&
                        txn.getAccount().getAvailableBalance()
                                .subtract(txn.getTypeTransaction().getValue())
                                .compareTo(txn.getValue()) < 0;

        return Mono.just(transaction)
                .filter(isAccountActive)
                .switchIfEmpty(Mono.error(
                        new TransactionRejectedException("Inactive or invalid account.")))
                .filter(isAccountRejected.negate())
                .switchIfEmpty(Mono.error(
                        new TransactionRejectedException("The account does not have sufficient funds.")));
    }
}
