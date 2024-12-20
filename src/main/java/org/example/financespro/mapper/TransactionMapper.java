package org.example.financespro.mapper;

import java.math.BigDecimal;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.model.Transaction;

public class TransactionMapper {

  private TransactionMapper() {}

  public static TransactionResponseDto toResponseDTO(Transaction transaction, BigDecimal remainingBalance) {
    return new TransactionResponseDto(
            transaction.getType(),
            transaction.getAmount(),
            transaction.getTransactionCost(),
            remainingBalance
    );
  }
}
