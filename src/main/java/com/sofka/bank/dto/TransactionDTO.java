package com.sofka.bank.dto;

import com.sofka.bank.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class TransactionDTO {
    private String id;

    @NotNull
    private TransactionType transactionType;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull
    private double fee;

    private LocalDateTime date;

    @NotBlank
    private String description;

    private BankAccountDTO bankAccount;

    public TransactionDTO(String id, TransactionType transactionType, double amount, double fee, LocalDateTime date,
                          String description, BankAccountDTO bankAccountDTO) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.description = description;
        this.bankAccount = bankAccountDTO;
    }


    public String getId() {return id;}

    public TransactionType getTransactionType() {
        return transactionType;
    }
    public double getAmount(){
        return amount;
    }

    public double getFee(){
        return fee;
    }

    public LocalDateTime getDate(){return date;}

    public String getDescription(){
        return description;
    }

    public BankAccountDTO getBankAccount() {
        return bankAccount;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFee(double fee){
        this.fee = fee;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBankAccount(BankAccountDTO bankAccount) {
        this.bankAccount = bankAccount;
    }
}