package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

public class ATMDepositStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.valueOf(2.00); // Fixed fee for ATM deposit
  }

  @Override
  public TransactionType getType() {
    return TransactionType.ATM_DEPOSIT;
  }
}
