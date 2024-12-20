package org.example.financespro.dto.response;

/** DTO for account responses. */
public final class AccountResponseDto {

  private final String id;
  private final String accountNumber;
  private final Double balance;

  public AccountResponseDto(String id, String accountNumber, Double balance) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.balance = balance;
  }

  public String getId() {
    return id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public Double getBalance() {
    return balance;
  }
}
