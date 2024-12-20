package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TRANSACTION_TYPE;

public class OtherAccountDepositStrategy implements TransactionCostStrategy {

  @Override
  public BigDecimal calculateCost(BigDecimal amount) {
    return BigDecimal.valueOf(1.50); // Fixed fee for transferring to another account
  }

  @Override
  public TRANSACTION_TYPE getType() {
    return TRANSACTION_TYPE.ONLINE_DEPOSIT;
  }
}
