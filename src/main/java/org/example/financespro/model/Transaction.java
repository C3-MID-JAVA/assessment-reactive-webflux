package org.example.financespro.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("transactions")
public class Transaction {

  @Id private final String id;
  private final String accountId;
  private final String type;
  private final BigDecimal amount;
  private final BigDecimal transactionCost;

  private Transaction(
      String id, String accountId, String type, BigDecimal amount, BigDecimal transactionCost) {
    this.id = id;
    this.accountId = accountId;
    this.type = type;
    this.amount = amount;
    this.transactionCost = transactionCost;
  }

  // Factory Method for creating a new Transaction
  public static Transaction create(
      String id, String accountId, String type, BigDecimal amount, BigDecimal transactionCost) {
    return new Transaction(id, accountId, type, amount, transactionCost);
  }

  // Getters
  public String getId() {
    return id;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getType() {
    return type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getTransactionCost() {
    return transactionCost;
  }

  // With methods for immutability
  public Transaction withId(String newId) {
    return new Transaction(newId, this.accountId, this.type, this.amount, this.transactionCost);
  }

  public Transaction withAccountId(String newAccountId) {
    return new Transaction(this.id, newAccountId, this.type, this.amount, this.transactionCost);
  }

  public Transaction withType(String newType) {
    return new Transaction(this.id, this.accountId, newType, this.amount, this.transactionCost);
  }

  public Transaction withAmount(BigDecimal newAmount) {
    return new Transaction(this.id, this.accountId, this.type, newAmount, this.transactionCost);
  }

  public Transaction withTransactionCost(BigDecimal newTransactionCost) {
    return new Transaction(this.id, this.accountId, this.type, this.amount, newTransactionCost);
  }
}
