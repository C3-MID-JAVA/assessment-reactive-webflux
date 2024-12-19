package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.service.strategy.TransactionStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {
    Mono<TransactionResponseDTO> create(TransactionRequestDTO transactionRequestDTO);
    Flux<TransactionResponseDTO> getAllByAccountNumber(String accountNumber);
}
