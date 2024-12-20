package com.sofkau.usrv_accounts_manager.dto;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Request body for creating a card")
public class CardDTO {
    @NotNull(message = "cardName" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardName" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Card name assigned to the card", example = "MAXCARD")
    private String cardName;

    @NotNull(message = "cardNumber" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardNumber" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Unique number assigned to the card", example = "1234567896325874")
    private String cardNumber;

    @NotNull(message = "cardType" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardType" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Type of the card, e.g., 'CREDIT', 'DEBIT'", example = "CREDIT")
    private String cardType;

    @NotNull(message = "cardStatus" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardStatus" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Status of the card (e.g., ACTIVE, INACTIVE)", example = "ACTIVE")
    private String cardStatus;

    @NotNull(message = "cardExpiryDate" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardExpiryDate" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Expiration date of the card in 'MM/YYYY' format", example = "12/2025")
    private String cardExpiryDate;

    @NotNull(message = "cardLimit" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "amount cannot be lest than 0")
    @Schema(description = "Initial card limit of the card in monetary value", example = "500.00")
    private BigDecimal cardLimit;

    @NotNull(message = "cardHolderName" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "cardHolderName" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Name of the card holder", example = "John Doe")
    private String cardHolderName;


    private AccountDTO account;

    @Schema(hidden = true)
    private List<TransactionDTO> transactions;

    public CardDTO() {
    }

    public CardDTO(String cardName, String cardNumber, String cardType, String cardStatus, String cardExpiryDate, BigDecimal cardLimit, String cardHolderName, AccountDTO account, List<TransactionDTO> transactions) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.cardExpiryDate = cardExpiryDate;
        this.cardLimit = cardLimit;
        this.cardHolderName = cardHolderName;
        this.account = account;
        this.transactions = transactions != null ? transactions : new ArrayList<>();
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public BigDecimal getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(BigDecimal cardLimit) {
        this.cardLimit = cardLimit;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
