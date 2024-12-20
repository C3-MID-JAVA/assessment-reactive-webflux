package com.sofka.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.util.ArrayList;
import java.util.List;



@Document(collection = "bank_account")
public class BankAccount {
    @Id
    private String id;

    @Field("account_number")
    private String accountNumber;

    @Field("account_holder")
    private String accountHolder;

    @Field("global_balance")
    private double globalBalance;

    @JsonIgnore
    private List<Transaction> transactions;

    public BankAccount() {}

    public BankAccount(String id, String accountNumber, String accountHolder, double globalBalance,
                       List<Transaction> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.globalBalance = globalBalance;
        this.transactions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getGlobalBalance() {
        return globalBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setGlobalBalance(double globalBalance) {
        this.globalBalance = globalBalance;

    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}