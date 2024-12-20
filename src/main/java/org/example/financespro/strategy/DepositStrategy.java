package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

public class DepositStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.ZERO;
  }

  @Override
  public TransactionType getType() {
    return TransactionType.BRANCH_DEPOSIT;
  }
}
