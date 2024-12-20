package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public class DepositAtmCostStrategy implements TransactionCostStrategy {
    @Override
    public BigDecimal calculateCost(BigDecimal amount) {
        return BigDecimal.valueOf(2);
    }
}
