package org.example.financespro.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** DTO for account creation requests. */
public final class AccountRequestDto {

  @NotBlank(message = "Account number is required.")
  private final String number;

  @NotNull(message = "Initial balance is required.")
  @DecimalMin(
          value = "0.0",
          inclusive = false,
          message = "Initial balance must be a positive value.")
  private final BigDecimal initialBalance;

  public AccountRequestDto(String number, BigDecimal initialBalance) {
    this.number = number;
    this.initialBalance = initialBalance;
  }

  public String getNumber() {
    return number;
  }

  public BigDecimal getInitialBalance() {
    return initialBalance;
  }
}
