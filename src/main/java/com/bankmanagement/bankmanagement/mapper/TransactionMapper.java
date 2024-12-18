package com.bankmanagement.bankmanagement.mapper;

import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.Transaction;

public class TransactionMapper {
    public static TransactionResponseDTO fromEntity(Transaction transaction){
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(transaction.getId());
        transactionResponseDTO.setFee(transaction.getFee());
        transactionResponseDTO.setNetAmount(transaction.getNetAmount());
        transactionResponseDTO.setType(transaction.getType());
        transactionResponseDTO.setTimestamp(transaction.getTimestamp());
        return transactionResponseDTO;
    }
}
