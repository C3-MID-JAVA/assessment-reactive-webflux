package edisonrmedina.CityBank.service.impl;

import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.entity.transaction.Transaction;
import edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy.TransactionCostStrategy;
import edisonrmedina.CityBank.enums.TransactionType;
import edisonrmedina.CityBank.repository.TransactionRepository;
import edisonrmedina.CityBank.service.TransactionsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final BankAccountServiceImp bankAccountService;
    private final Map<TransactionType, TransactionCostStrategy> costStrategies;
    private final TransactionRepository transactionRepository;

    public TransactionsServiceImpl(BankAccountServiceImp bankAccountService, Map<TransactionType, TransactionCostStrategy> costStrategies, TransactionRepository transactionRepository) {
        this.bankAccountService = bankAccountService;
        this.costStrategies = costStrategies;
        this.transactionRepository = transactionRepository;
    }

    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public Mono<Transaction> createTransaction(Transaction transaction) {
        return Mono.just(transaction)
                .flatMap(tx -> {
                    // Obtener directamente el objeto BankAccount
                    BankAccount account = tx.getBankAccount();
                    if (account == null) {
                        return Mono.error(new IllegalArgumentException("Cuenta bancaria no proporcionada"));
                    }

                    // Aquí puedes continuar con la lógica de la transacción
                    TransactionCostStrategy strategy = costStrategies.get(tx.getType());
                    if (strategy == null) {
                        return Mono.error(new IllegalArgumentException("Tipo de transacción no soportado"));
                    }

                    BigDecimal transactionCost = strategy.calculateCost(tx.getAmount());
                    tx.setTransactionCost(transactionCost);

                    BigDecimal newBalance;
                    switch (tx.getType()) {
                        case WITHDRAW_ATM:
                        case PURCHASE_PHYSICAL:
                        case PURCHASE_ONLINE:
                            newBalance = account.getBalance().subtract(tx.getAmount().add(transactionCost));
                            break;
                        case DEPOSIT_BRANCH:
                        case DEPOSIT_ATM:
                        case DEPOSIT_ACCOUNT:
                            newBalance = account.getBalance().add(tx.getAmount().subtract(transactionCost));
                            break;
                        case DEPOSIT_OUT:
                        default:
                            return Mono.error(new IllegalArgumentException("Tipo de transacción no soportado"));
                    }

                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente para realizar la transacción"));
                    }

                    account.setBalance(newBalance);
                    tx.setBankAccount(Optional.of(account));  // No es necesario envolver en Optional

                    return  transactionRepository.save(tx);
                });
    }


    public Mono<BigDecimal> calculateNewBalance(Transaction transaction, Map<TransactionType, TransactionCostStrategy> costStrategies ) {
        // Determinar el costo de la transacción
        TransactionCostStrategy strategy = costStrategies.get(transaction.getType());
        if (strategy == null) {
            return Mono.error(new IllegalArgumentException("Tipo de transacción no soportado"));
        }

        BigDecimal transactionCost = strategy.calculateCost(transaction.getAmount());
        BigDecimal newBalance = transaction.getBankAccount().getBalance();

        switch (transaction.getType()) {
            case WITHDRAW_ATM, PURCHASE_PHYSICAL, PURCHASE_ONLINE:
                newBalance = transaction.getBankAccount().getBalance().subtract(transaction.getAmount().add(transactionCost));
                break;
            case DEPOSIT_BRANCH, DEPOSIT_ATM, DEPOSIT_ACCOUNT:
                newBalance = transaction.getBankAccount().getBalance().add(transaction.getAmount().subtract(transactionCost));
                break;
            case DEPOSIT_OUT:
                return Mono.error(new IllegalArgumentException("Tipo de transacción no soportado"));
        }

        if(newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new IllegalArgumentException("Saldo insuficiente para realizar la transacción"));
        }

        transaction.getBankAccount().setBalance(newBalance);

        return Mono.just(transactionCost);
    }
}
