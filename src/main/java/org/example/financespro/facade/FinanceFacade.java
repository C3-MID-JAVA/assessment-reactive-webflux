package org.example.financespro.facade;

import org.example.financespro.dto.request.AccountRequestDto;
import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.AccountResponseDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.service.AccountService;
import org.example.financespro.service.TransactionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FinanceFacade {

  private final AccountService accountService;
  private final TransactionService transactionService;

  public FinanceFacade(AccountService accountService, TransactionService transactionService) {
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  public Mono<AccountResponseDto> createAccount(AccountRequestDto request) {
    return accountService.createAccount(request);
  }

  public Mono<AccountResponseDto> getAccountDetails(String accountNumber) {
    return accountService.getAccountDetailsByNumber(accountNumber);
  }

  public Mono<TransactionResponseDto> processTransaction(TransactionRequestDto request) {
    return transactionService.processTransaction(request);
  }
}
