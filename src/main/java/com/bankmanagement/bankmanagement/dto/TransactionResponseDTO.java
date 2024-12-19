package com.bankmanagement.bankmanagement.dto;

import com.bankmanagement.bankmanagement.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private String id;

    private double fee;

    private double netAmount;

    private TransactionType type;

    private LocalDateTime timestamp;
}
