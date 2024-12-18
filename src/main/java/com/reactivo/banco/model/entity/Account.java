package com.reactivo.banco.model.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "accounts")
public class Account {

    @Id
    private String id;
    private String AccountNumber;
    private BigDecimal balance;
    private String custumerId;
    private String cardId;

    public Account() {
    }

    public Account(String id, String accountNumber, BigDecimal balance, String custumerId, String cardId) {
        this.id = id;
        AccountNumber = accountNumber;
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
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
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
