package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.TransactionRequestDTO;
import ec.com.example.bank_account.dto.TransactionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionResponseDTO> createTransaction(TransactionRequestDTO transactionRequestDTO);

    Flux<TransactionResponseDTO> getAllTransactions();

}