package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

public class OnlinePurchaseStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.valueOf(5.00); // Fixed fee for online purchases
  }

  @Override
  public TransactionType getType() {
    return TransactionType.ONLINE_PURCHASE;
  }
}
