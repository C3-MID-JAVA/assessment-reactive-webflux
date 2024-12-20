package org.example.financespro.service.impl;

import java.math.BigDecimal;
import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.exception.CustomException;
import org.example.financespro.mapper.TransactionMapper;
import org.example.financespro.model.Account;
import org.example.financespro.model.TRANSACTION_TYPE;
import org.example.financespro.model.Transaction;
import org.example.financespro.repository.AccountRepository;
import org.example.financespro.repository.TransactionRepository;
import org.example.financespro.service.TransactionService;
import org.example.financespro.strategy.StrategyFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;
  private final StrategyFactory strategyFactory;

  public TransactionServiceImpl(
      AccountRepository accountRepository,
      TransactionRepository transactionRepository,
      StrategyFactory strategyFactory) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
    this.strategyFactory = strategyFactory;
  }

  @Override
  public Mono<TransactionResponseDto> processTransaction(TransactionRequestDto requestDTO) {
    return Mono.just(requestDTO)
        .filter(req -> req.getAccountId() != null && !req.getAccountId().isBlank())
        .switchIfEmpty(Mono.error(new CustomException("Account ID cannot be null or blank")))
        .filter(
            req ->
                req.getTransactionAmount() != null
                    && req.getTransactionAmount().compareTo(BigDecimal.ZERO) > 0)
        .switchIfEmpty(Mono.error(new CustomException("Transaction amount must be greater than 0")))
        .flatMap(
            req ->
                accountRepository
                    .findById(req.getAccountId())
                    .switchIfEmpty(
                        Mono.error(
                            new CustomException(
                                "Account not found with ID: " + req.getAccountId())))
                    .flatMap(
                        account -> {
                          TRANSACTION_TYPE transactionType =
                              TRANSACTION_TYPE.valueOf(req.getTransactionType().toUpperCase());
                          BigDecimal transactionCost =
                              strategyFactory
                                  .getStrategy(transactionType)
                                  .calculateCost(req.getTransactionAmount());
                          BigDecimal newBalance =
                              account
                                  .getBalance()
                                  .subtract(req.getTransactionAmount().add(transactionCost));

                          if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            return Mono.error(
                                new CustomException("Insufficient funds for transaction"));
                          }

                          Account updatedAccount = account.withBalance(newBalance);
                          return accountRepository
                              .save(updatedAccount)
                              .flatMap(
                                  savedAccount -> {
                                    Transaction transaction =
                                        Transaction.create(
                                            null, // MongoDB generates the ID
                                            savedAccount.getId(),
                                            transactionType.name(),
                                            req.getTransactionAmount(),
                                            transactionCost);
                                    return transactionRepository.save(transaction);
                                  })
                              .map(
                                  savedTransaction ->
                                      TransactionMapper.toResponseDTO(
                                          savedTransaction, newBalance));
                        }));
  }
}
