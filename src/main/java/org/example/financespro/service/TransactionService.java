package org.example.financespro.service;

import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import reactor.core.publisher.Mono;

public interface TransactionService {

  /**
   * Processes a financial transaction reactively.
   *
   * @param requestDTO the transaction details
   * @return a Mono containing the transaction response with cost and remaining balance
   */
  Mono<TransactionResponseDto> processTransaction(TransactionRequestDto requestDTO);
}
