package com.example.bank.service.impl;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.AccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
                .map(AccountMapper::toDTO)
                .switchIfEmpty(Flux.error(new RuntimeException("No accounts found.")));
    }

    @Override
    public Mono<AccountResponseDTO> getAccountById(String id) {
        return accountRepository.findById(id)
                .map(AccountMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found.")));
    }

    @Override
    public Mono<AccountResponseDTO> createAccount(AccountRequestDTO accountDTO) {
        return Mono.just(new AccountRequestDTO(accountDTO.getHolder(), accountDTO.getBalance()))
                .map(AccountMapper::toEntity)
                .flatMap(accountRepository::save)
                .map(AccountMapper::toDTO);
    }
}