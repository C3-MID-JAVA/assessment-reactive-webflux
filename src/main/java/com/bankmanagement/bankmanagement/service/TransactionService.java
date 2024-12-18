package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.service.strategy.TransactionStrategy;

import java.util.List;

public interface TransactionService {
    TransactionResponseDTO create(TransactionRequestDTO transactionRequestDTO);
    List<TransactionResponseDTO> getAllByAccountNumber(String accountNumber);
}
