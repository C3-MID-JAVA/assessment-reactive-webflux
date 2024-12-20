package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO);
}
