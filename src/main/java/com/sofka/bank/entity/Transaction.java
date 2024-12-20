package com.sofka.bank.entity;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Field("transaction_type")
    private TransactionType transactionType;

    private double amount;
    private double fee;
    private LocalDateTime date;
    private String description;

    @Field("bank_account_id")
    private BankAccount bankAccount;

    public Transaction() {}

    public Transaction(String id, TransactionType transactionType, double amount, double fee, LocalDateTime date,
                       String description,
                       BankAccount bankAccount) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.description = description;
        this.bankAccount = bankAccount;
    }


    public String getId() {
        return id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getFee() {
        return fee;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}