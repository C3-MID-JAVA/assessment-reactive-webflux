package edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy;

import java.math.BigDecimal;

public class WithdrawAtmCostStrategy implements TransactionCostStrategy{
    @Override
    public BigDecimal calculateCost(BigDecimal amount) {
        return BigDecimal.valueOf(1);
    }
}
