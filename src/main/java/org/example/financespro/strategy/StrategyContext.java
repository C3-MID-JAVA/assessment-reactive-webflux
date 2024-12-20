package org.example.financespro.strategy;

import java.math.BigDecimal;

public class StrategyContext {

  private TransactionCostStrategy strategy;

  public void setStrategy(TransactionCostStrategy strategy) {
    this.strategy = strategy;
  }

  public BigDecimal executeStrategy(BigDecimal amount) {
    return strategy.calculateCost(amount);
  }
}
