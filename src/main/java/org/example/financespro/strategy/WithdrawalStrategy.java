package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TRANSACTION_TYPE;

public class WithdrawalStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.valueOf(1.00); // Fixed fee for ATM withdrawal
  }

  @Override
  public TRANSACTION_TYPE getType() {
    return TRANSACTION_TYPE.ATM_WITHDRAWAL;
  }
}
