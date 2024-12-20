package org.example.financespro.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
public class Account {

  @Id private final String id;

  @Indexed(unique = true)
  private final String accountNumber;

  private final BigDecimal balance;

  private Account(String id, String accountNumber, BigDecimal balance) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.balance = balance;
  }

  // Factory Method for creating a new Account
  public static Account create(String id, String accountNumber, BigDecimal balance) {
    return new Account(id, accountNumber, balance);
  }

  // Getters
  public String getId() {
    return id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  // With methods for immutability
  public Account withId(String newId) {
    return new Account(newId, this.accountNumber, this.balance);
  }

  public Account withAccountNumber(String newAccountNumber) {
    return new Account(this.id, newAccountNumber, this.balance);
  }

  public Account withBalance(BigDecimal newBalance) {
    return new Account(this.id, this.accountNumber, newBalance);
  }
}
