package org.example.financespro.config;

import org.example.financespro.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {

  @Bean
  public StrategyContext strategyContext() {
    return new StrategyContext();
  }

  @Bean
  public TransactionCostStrategy depositStrategy() {
    return new DepositStrategy();
  }

  @Bean
  public TransactionCostStrategy atmDepositStrategy() {
    return new ATMDepositStrategy();
  }

  @Bean
  public TransactionCostStrategy otherAccountDepositStrategy() {
    return new OtherAccountDepositStrategy();
  }

  @Bean
  public TransactionCostStrategy physicalPurchaseStrategy() {
    return new PhysicalPurchaseStrategy();
  }

  @Bean
  public TransactionCostStrategy onlinePurchaseStrategy() {
    return new OnlinePurchaseStrategy();
  }

  @Bean
  public TransactionCostStrategy withdrawalStrategy() {
    return new WithdrawalStrategy();
  }
}
