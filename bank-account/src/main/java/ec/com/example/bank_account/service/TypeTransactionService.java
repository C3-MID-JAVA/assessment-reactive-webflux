package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.TypeTransactionRequestDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeTransactionService {

    Mono<TypeTransactionResponseDTO> createTypeTransaction(TypeTransactionRequestDTO typeTransaction);

    Flux<TypeTransactionResponseDTO> getAllTypeTransactions();

}