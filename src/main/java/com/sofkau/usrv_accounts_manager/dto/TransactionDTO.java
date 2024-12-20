package com.sofkau.usrv_accounts_manager.dto;



import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;


@Schema(description = "Request representing a transaction with all its details")
public class TransactionDTO {

    @NotNull(message = "description" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "description" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Description of the transaction", example = "ATM Withdrawal")
    private String description;

    @NotNull(message = "accountNumber" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "amount cannot be lest than 0")
    @Schema(description = "Amount for the transaction", example = "100.00")
    private BigDecimal amount;

    @NotNull(message = "transactionType" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "transactionType" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Type of the transaction (e.g., BD, ATM)", example = "ATM")
    private String transactionType;

    @NotNull(message = "transactionFee" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "amount cannot be lest than 0")
    @Schema(description = "Fee associated with the transaction", example = "0")
    private BigDecimal transactionFee;

    @NotNull(message = "account" + ConstansTrType.NOT_NULL_FIELD)
    @Schema(hidden = true)
    private AccountDTO account;

    @NotNull(message = "card" + ConstansTrType.NOT_NULL_FIELD)
    @Schema(hidden = true)
    private CardDTO card;


    @Schema(hidden = true)
    private String website;
    @Schema(hidden = true)
    private String marketName;

    @Schema(hidden = true)
    private String atmName;
    @Schema(hidden = true)
    private String operationType;

    @Schema(hidden = true)
    private String branchName;

    @Schema(hidden = true)
    private AccountModel accountReceiver;



    public TransactionDTO(String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, AccountDTO account, CardDTO card) {
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.account = account;
        this.card = card;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }


    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public AccountModel getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(AccountModel accountReceiver) {
        this.accountReceiver = accountReceiver;
    }
}
