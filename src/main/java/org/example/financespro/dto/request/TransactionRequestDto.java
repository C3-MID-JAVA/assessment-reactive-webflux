package org.example.financespro.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** DTO for transaction requests. */
public final class TransactionRequestDto {

  @NotBlank(message = "Account ID is required.")
  private final String accountId;

  @NotBlank(message = "Transaction type is required.")
  private final String transactionType;

  @NotNull(message = "Transaction amount is required.")
  @DecimalMin(value = "0.01", inclusive = false, message = "Transaction amount must be positive.")
  private final BigDecimal transactionAmount;

  public TransactionRequestDto(
      String accountId, String transactionType, BigDecimal transactionAmount) {
    this.accountId = accountId;
    this.transactionType = transactionType;
    this.transactionAmount = transactionAmount;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }
}
