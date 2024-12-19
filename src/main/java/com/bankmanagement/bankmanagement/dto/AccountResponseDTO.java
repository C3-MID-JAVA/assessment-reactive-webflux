package com.bankmanagement.bankmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private double balance;
    private String userId;
}
