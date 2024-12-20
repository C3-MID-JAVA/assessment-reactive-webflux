package co.com.sofka.bankingaccount.bankingaccount.factory;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class TransactionFactory {
    private final Map<String, BiFunction<BigDecimal, String, TransactionBase>> transactionStrategy = new HashMap<>();

    // Predicate para validar si el monto es válido
    private final Predicate<BigDecimal> isAmountValid = amount -> amount.compareTo(BigDecimal.ZERO) > 0;

    // Function para calcular costos adicionales dependiendo del tipo de transacción
    private final Function<String, BigDecimal> calculateCost = type -> {
        return switch (type.toUpperCase()) {
            case "ATM_DEPOSIT" -> BigDecimal.valueOf(2); //Costo de depósito en cajero
            case "ATM_WITHDRAWAL" -> BigDecimal.valueOf(1); // Costo de retiro en cajero
            case "CARD_WEB_BUY" -> BigDecimal.valueOf(5); // Costo de compra en web
            case "DEPOSIT_FROM_ANOTHER_ACCOUNT" -> BigDecimal.valueOf(1.5); // Costo por depósito de otra cuenta
            default -> BigDecimal.ZERO; // Sin costo para otras transacciones
        };
    };

    public TransactionFactory() {
        // Strategy Registry
        transactionStrategy.put("ATM_DEPOSIT", AtmDeposit::new);
        transactionStrategy.put("ATM_WITHDRAWAL", AtmWithdrawal::new);
        transactionStrategy.put("BRANCH_DEPOSIT", BranchDeposit::new);
        transactionStrategy.put("CARD_PHYSICAL_BUY", CardPhysicalBuy::new);
        transactionStrategy.put("CARD_WEB_BUY", CardWebBuy::new);
        transactionStrategy.put("DEPOSIT_FROM_ANOTHER_ACCOUNT", DepositFromAnotherAccount::new);
    }

    public TransactionBase createTransaction(String type, BigDecimal amount, String accountId){
        // Validar si el monto es válido usando Predicate
        if (!isAmountValid.test(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        BiFunction<BigDecimal, String, TransactionBase> strategy = transactionStrategy.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Transaction Type not valid: " + type);
        }

        // Crear la transacción
        TransactionBase transaction = strategy.apply(amount, accountId);

        // Calcular y asignar el costo usando Function
        BigDecimal cost = calculateCost.apply(type);
        transaction.setCost(cost);

        return transaction;
    }
}
