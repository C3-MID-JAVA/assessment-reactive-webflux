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
    public TransactionResponseDTO create(TransactionRequestDTO transactionRequestDTO) {
        Account account = accountRepository.findByAccountNumber(transactionRequestDTO.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

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
        Transaction savedTransaction = transactionRepository.save(transaction);

        account.setBalance(balance);
        accountRepository.save(account);

        return TransactionMapper.fromEntity(savedTransaction);
    }

    @Override
    public List<TransactionResponseDTO> getAllByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return transactionRepository.findAllByAccountId(account.getId()).stream().map(TransactionMapper::fromEntity).toList();
    }
}
