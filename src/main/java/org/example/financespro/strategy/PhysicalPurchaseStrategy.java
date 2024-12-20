package org.example.financespro.strategy;

import java.math.BigDecimal;

import org.example.financespro.model.TRANSACTION_TYPE;

public class PhysicalPurchaseStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.ZERO; // No cost for physical purchases
  }

  @Override
  public TRANSACTION_TYPE getType() {
    return TRANSACTION_TYPE.PHYSICAL_PURCHASE;
  }
}
