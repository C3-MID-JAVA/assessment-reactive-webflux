package com.example.bank.service;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountResponseDTO> getAllAccounts();
    Mono<AccountResponseDTO> getAccountById(String id);
    Mono<AccountResponseDTO> createAccount(AccountRequestDTO accountDTO);
}