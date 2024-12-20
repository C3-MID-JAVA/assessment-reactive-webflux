package org.example.financespro.dto.response;

import java.math.BigDecimal;

public final class TransactionResponseDto {

  private final String transactionType;
  private final BigDecimal transactionAmount;
  private final BigDecimal transactionCost;
  private final BigDecimal remainingBalance;

  public TransactionResponseDto(String transactionType, BigDecimal transactionAmount, BigDecimal transactionCost, BigDecimal remainingBalance) {
    this.transactionType = transactionType;
    this.transactionAmount = transactionAmount;
    this.transactionCost = transactionCost;
    this.remainingBalance = remainingBalance;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }

  public BigDecimal getTransactionCost() {
    return transactionCost;
  }

  public BigDecimal getRemainingBalance() {
    return remainingBalance;
  }
}
