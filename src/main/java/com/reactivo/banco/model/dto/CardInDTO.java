package com.reactivo.banco.model.dto;

public class CardInDTO {

    private String cardNumber;
    private String type;
    private String accountId;

    public CardInDTO() {
    }

    public CardInDTO(String cardNumber, String type, String accountId) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
