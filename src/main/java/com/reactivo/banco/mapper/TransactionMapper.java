package com.reactivo.banco.mapper;

import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import com.reactivo.banco.model.entity.Transaction;

public class TransactionMapper {

    public static Transaction toEntity(TransactionInDTO transactionInDTO) {
        if (transactionInDTO == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setDescription(transactionInDTO.getDescription());
        transaction.setAmount(transactionInDTO.getAmount());
        transaction.setTransactionType(transactionInDTO.getTransactionType());
        transaction.setDate(transactionInDTO.getDate());
        transaction.setAccountId(transactionInDTO.getAccountId());

        return transaction;
    }

    public static TransactionOutDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionOutDTO transactionOutDTO = new TransactionOutDTO();
        transactionOutDTO.setId(transaction.getId());
        transactionOutDTO.setDescription(transaction.getDescription());
        transactionOutDTO.setAmount(transaction.getAmount());
        transactionOutDTO.setTransactionType(transaction.getTransactionType());
        transactionOutDTO.setDate(transaction.getDate());
        transactionOutDTO.setAccountId(transaction.getAccountId());

        return transactionOutDTO;
    }
}
