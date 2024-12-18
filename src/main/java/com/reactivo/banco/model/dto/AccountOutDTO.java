package com.reactivo.banco.model.dto;

import java.math.BigDecimal;

public class AccountOutDTO {

    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private String custumerId;
    private String cardId;

    public AccountOutDTO() {
    }

    public AccountOutDTO(String id, String accountNumber, BigDecimal balance, String custumerId, String cardId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.custumerId = custumerId;
        this.cardId = cardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCustumerId() {
        return custumerId;
    }

    public void setCustumerId(String custumerId) {
        this.custumerId = custumerId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
