package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public class DepositBranchCostStrategy implements TransactionCostStrategy {
    @Override
    public BigDecimal calculateCost(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
