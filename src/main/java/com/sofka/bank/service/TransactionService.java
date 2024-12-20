package com.sofka.bank.service;

import com.sofka.bank.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionDTO> registerTransaction(String accountId, TransactionDTO transactionDTO);
    Mono<Double> getGlobalBalance(String accountId);
}