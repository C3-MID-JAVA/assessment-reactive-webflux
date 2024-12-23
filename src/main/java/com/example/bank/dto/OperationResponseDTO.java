package com.example.bank.dto;

import com.example.bank.utils.operations.OperationTypesEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public class OperationResponseDTO {
    @Schema(description = "Id of operation")
    private String id;

    @Schema(description = "Cash value of operation")
    private BigDecimal value;

    @Schema(description = "Type of operation")
    private OperationTypesEnum type;

    @Schema(description = "Id of account associated to operation")
    private String accountId;

    public OperationResponseDTO() {}

    public OperationResponseDTO(BigDecimal value, OperationTypesEnum type, String accountId) {
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.value = value;
        this.type = type;
        this.accountId = accountId;
    }

    public OperationResponseDTO(String id, BigDecimal value, OperationTypesEnum type, String accountId) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public OperationTypesEnum getType() {
        return type;
    }

    public void setType(OperationTypesEnum type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
