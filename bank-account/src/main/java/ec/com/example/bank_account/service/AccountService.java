package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<AccountResponseDTO> createAccount(AccountRequestDTO accountRequestDTO);

    Flux<AccountResponseDTO> getAllAccounts();

}