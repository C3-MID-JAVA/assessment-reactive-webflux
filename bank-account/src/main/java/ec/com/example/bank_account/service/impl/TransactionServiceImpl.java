package ec.com.example.bank_account.service.impl;

import ec.com.example.bank_account.dto.TransactionRequestDTO;
import ec.com.example.bank_account.dto.TransactionResponseDTO;
import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TransactionMapper;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.repository.TransactionRepository;
import ec.com.example.bank_account.service.TransactionService;

import ec.com.example.bank_account.util.process.BalanceCalculator;
import ec.com.example.bank_account.util.process.ValidateTransaction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final ValidateTransaction validateTransaction;
    private final BalanceCalculator balanceCalculator;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository,
                                  TransactionMapper transactionMapper, BalanceCalculator balanceCalculator, ValidateTransaction validateTransaction) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.balanceCalculator = balanceCalculator;
        this.validateTransaction = validateTransaction;
    }

    @Override
    public Mono<TransactionResponseDTO> createTransaction(TransactionRequestDTO transactionRequestDTO) {
        return Mono.just(transactionMapper.mapToEntity(transactionRequestDTO))
                .flatMap(validateTransaction::validateTransaction)
                .flatMap(this::updateBalanceAndSave)
                .map(transactionMapper::mapToDTO);
    }

    @Override
    public Flux<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .map(transactionMapper::mapToDTO)
                .switchIfEmpty(Flux.error(new EmptyCollectionException("No transactions records found.")));
    }

    public Mono<Transaction> updateBalanceAndSave(Transaction transaction) {
        BigDecimal newBalance = balanceCalculator.calculate(
                transaction,
                transaction.getAccount().getAvailableBalance()
        );

        transaction.getAccount().setAvailableBalance(newBalance);

        return accountRepository.save(transaction.getAccount())
                .then(transactionRepository.save(transaction));
    }
}