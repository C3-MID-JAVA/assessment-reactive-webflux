package org.bankAccountManager.util.function;

import org.bankAccountManager.DTO.request.TransactionRequestDTO;
import org.bankAccountManager.DTO.response.TransactionResponseDTO;
import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Transaction;
import org.springframework.beans.BeanUtils;

import java.util.function.Function;

public class TransactionMappers {
    public static final Function<TransactionRequestDTO, Transaction> toTransaction = dto -> {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(dto, transaction);
        if (dto.getSourceAccount() != null) {
            Account sourceAccount = new Account();
            BeanUtils.copyProperties(dto.getSourceAccount(), sourceAccount);
            transaction.setSourceAccount(sourceAccount);
        }
        if (dto.getDestinationAccount() != null) {
            Account destinationAccount = new Account();
            BeanUtils.copyProperties(dto.getDestinationAccount(), destinationAccount);
            transaction.setDestinationAccount(destinationAccount);
        }
        return transaction;
    };

    public static final Function<Transaction, TransactionResponseDTO> toTransactionResponseDTO = transaction -> {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    };
}
