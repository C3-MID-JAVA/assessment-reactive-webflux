package org.bankAccountManager.util.predicate;

import org.bankAccountManager.entity.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class TransactionValidations {
    public static Predicate<Account> hasSufficientBalance(BigDecimal amount) {
        return account -> account != null && account.getBalance().compareTo(amount) >= 0;
    }


    public static Predicate<String> isValidTransactionType(Map<String,BigDecimal> validTypes) {
        return validTypes::containsKey;
    }
}
