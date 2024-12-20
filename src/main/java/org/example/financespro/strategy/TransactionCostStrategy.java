package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TRANSACTION_TYPE;

@FunctionalInterface
public interface TransactionCostStrategy {
  BigDecimal calculateCost(BigDecimal amount);

  default TRANSACTION_TYPE getType() {
    return null; // Optional, override in subclasses if needed
  }
}
