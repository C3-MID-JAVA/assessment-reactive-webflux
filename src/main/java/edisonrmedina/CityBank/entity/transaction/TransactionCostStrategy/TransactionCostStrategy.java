package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public interface TransactionCostStrategy {
    BigDecimal calculateCost(BigDecimal amount);
}
