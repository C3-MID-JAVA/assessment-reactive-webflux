package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.TransactionRequestDTO;
import ec.com.example.bank_account.dto.TransactionResponseDTO;
import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.exception.RecordNotFoundException;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.repository.TypeTransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransactionMapper {
    private final AccountRepository accountRepository;
    private final TypeTransactionRepository typeTransactionRepository;
    private final AccountMapper accountMapper;
    private final TypeTransactionMapper typeTransactionMapper;

    public TransactionMapper(AccountMapper accountMapper, AccountRepository accountRepository,
                             TypeTransactionRepository typeTransactionRepository,
                             TypeTransactionMapper typeTransactionMapper) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.typeTransactionRepository = typeTransactionRepository;
        this.typeTransactionMapper = typeTransactionMapper;
    }

    public Transaction mapToEntity(TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO == null) {
            return null;
        }

        Transaction transaction = new Transaction();

        accountRepository.findByNumber(transactionRequestDTO.getAccountNumber())
                .switchIfEmpty(Mono.error(new RecordNotFoundException("Account not found.")))
                .doOnNext(transaction::setAccount)
                .subscribe();

        typeTransactionRepository.findById(transactionRequestDTO.getTypeTransactionId())
                .switchIfEmpty(Mono.error(new RecordNotFoundException("TypeTransaction not found.")))
                .doOnNext(transaction::setTypeTransaction)
                .subscribe();
        transaction.setTypeTransactionIdentify(transactionRequestDTO.getTypeTransactionId());
        transaction.setAccountNumber(transactionRequestDTO.getAccountNumber());
        transaction.setDetails(transactionRequestDTO.getDetails());
        transaction.setDate(transactionRequestDTO.getDate());
        transaction.setValue(transactionRequestDTO.getValue());
        transaction.setStatus(transactionRequestDTO.getStatus());

        return transaction;

    }

    public TransactionResponseDTO mapToDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponseDTO(
                transaction.getAccountNumber(),
                transaction.getTypeTransactionIdentify(),
                transaction.getValue(),
                transaction.getDate(),
                transaction.getStatus(),
                Mono.just(accountMapper.mapToDTO(transaction.getAccount())),
                Mono.just(typeTransactionMapper.mapToDTO(transaction.getTypeTransaction()))
        );
    }
}