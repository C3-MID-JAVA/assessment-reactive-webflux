package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    Mono<AccountResponseDTO> create(AccountRequestDTO accountRequestDTO);
    Flux<AccountResponseDTO> getAllByUserId(String userId);
    Mono<AccountResponseDTO> findByAccountNumber(String accountNumber);
}
