package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

public class OtherAccountDepositStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.valueOf(1.50); // Fixed fee for transferring to another account
  }

  @Override
  public TransactionType getType() {
    return TransactionType.ONLINE_DEPOSIT;
  }
}
