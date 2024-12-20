package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeAccountService {

    Mono<TypeAccountResponseDTO> createTypeAccount(TypeAccountRequestDTO typeAccount);

    Flux<TypeAccountResponseDTO> getAllTypeAccount();

}
