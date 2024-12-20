package org.example.financespro.strategy;

import java.math.BigDecimal;
import org.example.financespro.model.TransactionType;

@FunctionalInterface
public interface TransactionCostStrategy {
  BigDecimal calculateCost(BigDecimal amount);

  default TransactionType getType() {
    return null; // Optional, override in subclasses if needed
  }
}
