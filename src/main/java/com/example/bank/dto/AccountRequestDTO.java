package com.example.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRequestDTO {
    @Schema(description = "Id of account", example = "27afdfd5-", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

    @Schema(description = "Account holder's name", example = "Nat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String holder;

    @Schema(description = "Balance of account", example = "123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal balance;

    @Schema(description = "List of operation associated to account", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<OperationRequestDTO> operations;

    public void init() {
        this.id = this.id == null ? UUID.randomUUID().toString().substring(0,9) : this.id;
        this.balance = BigDecimal.ZERO;
        this.operations = new ArrayList<>();
    }

    public AccountRequestDTO() {}

    // Create with default balance
    public AccountRequestDTO(String holder) {
        this.init();
        this.holder = holder;
    }

    // Create with custom balance
    public AccountRequestDTO(String holder, BigDecimal balance) {
        this.init();
        this.holder = holder;
        this.balance = balance;
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

    public List<OperationRequestDTO> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationRequestDTO> operations) {
        this.operations = operations;
    }
}
