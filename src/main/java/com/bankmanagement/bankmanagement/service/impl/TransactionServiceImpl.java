package com.bankmanagement.bankmanagement.service.impl;

import com.bankmanagement.bankmanagement.dto.TransactionRequestDTO;
import com.bankmanagement.bankmanagement.dto.TransactionResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.mapper.TransactionMapper;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.Transaction;
import com.bankmanagement.bankmanagement.repository.AccountRepository;
import com.bankmanagement.bankmanagement.repository.TransactionRepository;
import com.bankmanagement.bankmanagement.service.TransactionService;
import com.bankmanagement.bankmanagement.service.strategy.TransactionStrategy;
import com.bankmanagement.bankmanagement.service.strategy.TransactionStrategyFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionStrategyFactory strategyFactory;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionStrategyFactory strategyFactory) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public Mono<TransactionResponseDTO> create(TransactionRequestDTO transactionRequestDTO) {
        return accountRepository.findByAccountNumber(transactionRequestDTO.getAccountNumber())
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .flatMap(account -> {
                    TransactionStrategy transactionStrategy = strategyFactory.getStrategy(transactionRequestDTO.getType());
                    double fee = transactionStrategy.calculateFee();
                    double balance = transactionStrategy.calculateBalance(account.getBalance(), transactionRequestDTO.getAmount());
                    double netAmount = transactionRequestDTO.getAmount() - fee;

                    Transaction transaction = new Transaction();
                    transaction.setAmount(transactionRequestDTO.getAmount());
                    transaction.setFee(fee);
                    transaction.setNetAmount(netAmount);
                    transaction.setType(transactionRequestDTO.getType());
                    transaction.setAccountId(account.getId());
                    transaction.setTimestamp(LocalDateTime.now());

                    return transactionRepository.save(transaction)
                            .flatMap(savedTransaction -> {
                                account.setBalance(balance);
                                return accountRepository.save(account)
                                        .thenReturn(TransactionMapper.fromEntity(savedTransaction));
                            });
                });
    }

    @Override
    public Flux<TransactionResponseDTO> getAllByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .flatMapMany(account -> {
                    return transactionRepository.findAllByAccountId(account.getId())
                            .map(TransactionMapper::fromEntity);
                });
    }
}
