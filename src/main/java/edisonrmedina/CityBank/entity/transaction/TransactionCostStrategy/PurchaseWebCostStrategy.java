package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public class PurchaseWebCostStrategy implements TransactionCostStrategy {
    @Override
    public BigDecimal calculateCost(BigDecimal amount) {
        return BigDecimal.valueOf(5);
    }
}
