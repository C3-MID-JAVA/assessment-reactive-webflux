package com.bankmanagement.bankmanagement.dto;

import com.bankmanagement.bankmanagement.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;
}
