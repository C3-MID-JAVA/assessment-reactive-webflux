package ec.com.example.bank_account.util.process;

import ec.com.example.bank_account.entity.Transaction;

import java.math.BigDecimal;

@FunctionalInterface
public interface BalanceCalculator {
    BigDecimal calculate(Transaction transaction, BigDecimal currentBalance);
}