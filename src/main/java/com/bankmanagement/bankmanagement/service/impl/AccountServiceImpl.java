package com.bankmanagement.bankmanagement.service.impl;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.mapper.AccountMapper;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.repository.AccountRepository;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.AccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<AccountResponseDTO> create(AccountRequestDTO accountRequestDTO) {
        return userRepository.findById(accountRequestDTO.getUserId())
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .flatMap(user -> {
                    Account account = new Account();
                    account.setAccountNumber(UUID.randomUUID().toString().substring(0, 8));
                    account.setBalance(0);
                    account.setUserId(user.getId());
                    return accountRepository.save(account);
                })
                .map(AccountMapper::fromEntity);
    }

    @Override
    public Flux<AccountResponseDTO> getAllByUserId(String userId){
        return accountRepository.findByUserId(userId)
                .map(AccountMapper::fromEntity);
    }

    @Override
    public Mono<AccountResponseDTO> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .map(AccountMapper::fromEntity);
    }
}
