package com.sofka.bank.dto;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;


public class BankAccountDTO {

    @NotNull
    private String id;

    @NotBlank(message = "Account number cannot be blank")
    @Pattern(regexp = "^\\d{7}$", message = "Account number must be 7 digits")
    private String accountNumber;

    @NotNull
    @NotEmpty(message = "Account holder name cannot be empty")
    private String accountHolder;

    @PositiveOrZero
    private double globalBalance;


    private List<TransactionDTO> transactions;

    public BankAccountDTO(String id, String accountNumber, String accountHolder, double globalBalance,
                          List<TransactionDTO> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.globalBalance = globalBalance;
        this.transactions = new ArrayList<>();
    }

    //Getters y Setters
    public String getId(){
        return id;
    }

    public void setId (String id){
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }
   public void setAccountHolder(String accountHolder){
        this.accountHolder = accountHolder;
   }

   public Double getGlobalBalance() { return globalBalance;}
    public void setGlobalBalance(double globalBalance){
        this.globalBalance = globalBalance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> vehicles) {
        this.transactions = vehicles;
    }

}