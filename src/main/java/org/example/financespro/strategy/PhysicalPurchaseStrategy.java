package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

public class PhysicalPurchaseStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.ZERO; // No cost for physical purchases
  }

  @Override
  public TransactionType getType() {
    return TransactionType.PHYSICAL_PURCHASE;
  }
}
