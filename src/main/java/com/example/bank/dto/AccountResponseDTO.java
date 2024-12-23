package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountResponseDTO {
    @Schema(description = "Id of account")
    private String id;

    @Schema(description = "Account holder's name")
    private String holder;

    @Schema(description = "Balance of account")
    private BigDecimal balance;

    @Schema(description = "List of operation associated to account")
    private List<OperationResponseDTO> operations;

    public AccountResponseDTO() {}

    public AccountResponseDTO(String id, String holder, BigDecimal balance, List<OperationResponseDTO> operations) {
        this.id = id;
        this.holder = holder;
        this.balance = balance;
        this.operations = new ArrayList<>(operations);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<OperationResponseDTO> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationResponseDTO> operations) {
        this.operations = operations;
    }
}
