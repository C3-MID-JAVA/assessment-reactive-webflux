package com.sofka.bank.service.impl;

import com.sofka.bank.dto.TransactionDTO;
import com.sofka.bank.entity.BankAccount;
import com.sofka.bank.entity.Transaction;
import com.sofka.bank.entity.TransactionType;
import com.sofka.bank.exceptions.AccountNotFoundException;
import com.sofka.bank.exceptions.InsufficientFundsException;
import com.sofka.bank.mapper.DTOMapper;
import com.sofka.bank.repository.BankAccountRepository;
import com.sofka.bank.repository.TransactionRepository;
import com.sofka.bank.service.TransactionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  BankAccountRepository bankAccountRepository){
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final Consumer<Transaction> logTransaction = transaction -> logger.info("Transaction successfully registered: {}", transaction.getId());

    @Override
    public Mono<TransactionDTO> registerTransaction(String accountId, TransactionDTO transactionDTO) {
        TransactionType transactionType = transactionDTO.getTransactionType();
        double fee = transactionType.getFee();

        return bankAccountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account with ID " + accountId + " not found")))
                .flatMap(account -> {
                    if (isTransactionTypeWithFee.test(transactionType)) {
                        if (account.getGlobalBalance() < transactionDTO.getAmount() + fee) {
                            return Mono.error(new InsufficientFundsException("Insufficient balance for transaction"));
                        }
                    }


        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setFee(fee);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setBankAccount(account);

        account.setGlobalBalance(account.getGlobalBalance() - transactionDTO.getAmount() - fee);

                    return bankAccountRepository.save(account)
                            .then(transactionRepository.save(transaction))
                            .doOnNext(logTransaction)
                            .map(DTOMapper::toTransactionDTO);
                });
    }

    private final Predicate<TransactionType> isTransactionTypeWithFee =
        transactionType ->  transactionType == TransactionType.WITHDRAW_ATM ||
                transactionType == TransactionType.ONLINE_PURCHASE ||
                transactionType == TransactionType.DEPOSIT_ATM ||
                transactionType == TransactionType.DEPOSIT_OTHER_ACCOUNT ||
                transactionType == TransactionType.BRANCH_DEPOSIT ||
                transactionType == TransactionType.ONSITE_CARD_PURCHASE;


    @Override
    public Mono<Double> getGlobalBalance(String accountId) {
        return bankAccountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account with ID " + accountId + " not found")))
                .map(BankAccount::getGlobalBalance);
    }


}