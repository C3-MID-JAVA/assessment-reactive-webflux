package ec.com.example.bank_account.util.process;

import ec.com.example.bank_account.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceCalculatorImpl implements BalanceCalculator {

    @Override
    public BigDecimal calculate(Transaction transaction, BigDecimal currentBalance) {
        return transaction.getTypeTransaction().getDiscount() ?
                currentBalance.subtract(transaction.getValue().add(transaction.getTypeTransaction().getValue())) :
                currentBalance.add(transaction.getValue().add(transaction.getTypeTransaction().getValue()));
    }
}