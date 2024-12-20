package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public class DepositAccountCostStrategy implements TransactionCostStrategy{
    @Override
    public BigDecimal calculateCost(BigDecimal amount) {
        return BigDecimal.valueOf(1.5);
    }
}
