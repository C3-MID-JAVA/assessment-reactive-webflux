package com.reactivo.banco.service;

import com.reactivo.banco.model.dto.AccountInDTO;
import com.reactivo.banco.model.dto.AccountOutDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface AccountService {

    Mono<AccountOutDTO> createAccount(AccountInDTO cuentaInDTO);

    Flux<AccountOutDTO> getAllAccounts();

    Mono<AccountOutDTO> getAccountById(String id);

    Mono<AccountOutDTO> updateAccount(String id, AccountInDTO cuentaInDTO);

    Mono<Void> deleteAccount(String id);
}
