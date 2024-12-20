package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TRANSACTION_TYPE;

public class DepositStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.ZERO;
  }

  @Override
  public TRANSACTION_TYPE getType() {
    return TRANSACTION_TYPE.BRANCH_DEPOSIT;
  }
}
