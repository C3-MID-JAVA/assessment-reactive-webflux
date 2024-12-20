package org.example.financespro.service.impl;

import java.math.BigDecimal;
import org.example.financespro.dto.request.AccountRequestDto;
import org.example.financespro.dto.response.AccountResponseDto;
import org.example.financespro.exception.CustomException;
import org.example.financespro.mapper.AccountMapper;
import org.example.financespro.model.Account;
import org.example.financespro.repository.AccountRepository;
import org.example.financespro.service.AccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Mono<AccountResponseDto> createAccount(AccountRequestDto requestDTO) {
    return Mono.just(requestDTO)
        .filter(req -> req.getNumber() != null && !req.getNumber().isBlank())
        .switchIfEmpty(Mono.error(new CustomException("Account number cannot be null or blank")))
        .filter(
            req ->
                req.getInitialBalance() != null
                    && req.getInitialBalance().compareTo(BigDecimal.ZERO) > 0)
        .switchIfEmpty(Mono.error(new CustomException("Initial balance must be a positive value")))
        .flatMap(
            req ->
                accountRepository
                    .findByAccountNumber(req.getNumber())
                    .flatMap(
                        existing ->
                            Mono.error(
                                new CustomException(
                                    "Account number already exists: " + req.getNumber())))
                    .then(Mono.just(req)))
        .map(
            req ->
                Account.create(
                    null, req.getNumber(), req.getInitialBalance())) // Use factory method
        .flatMap(accountRepository::save)
        .map(AccountMapper::toResponseDTO);
  }

  @Override
  public Mono<AccountResponseDto> getAccountDetailsByNumber(String accountNumber) {
    return accountRepository
        .findByAccountNumber(accountNumber)
        .map(AccountMapper::toResponseDTO)
        .switchIfEmpty(
            Mono.error(new CustomException("Account not found with number: " + accountNumber)));
  }
}
