package ec.com.example.bank_account.service;

import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.TransactionRequestDTO;
import ec.com.example.bank_account.dto.TransactionResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.entity.TypeTransaction;
import ec.com.example.bank_account.exception.EmptyCollectionException;
import ec.com.example.bank_account.mapper.TransactionMapper;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.repository.TransactionRepository;
import ec.com.example.bank_account.service.impl.TransactionServiceImpl;
import ec.com.example.bank_account.util.process.BalanceCalculator;
import ec.com.example.bank_account.util.process.ValidateTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TransactionServiceImplTest {
    
    @Mock
    private TransactionMapper transactionMapper;
    
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ValidateTransaction validateTransaction;

    @Mock
    private BalanceCalculator balanceCalculator;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private Account account;
    private TransactionRequestDTO transactionRequestDTO;
    private TransactionResponseDTO transactionResponseDTO;

    @BeforeEach
    public void setUp() {
        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");
        TypeAccountResponseDTO typeAccountResponse = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponse));

        account = new Account();
        account.setNumber("2200000000");
        account.setAvailableBalance(new BigDecimal(100));
        account.setRetainedBalance(new BigDecimal(0));
        account.setStatus("ACTIVE");

        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setValue(new BigDecimal(100));
        typeTransaction.setTransactionCost(false);
        typeTransaction.setDiscount(true);

        transaction = new Transaction();
        transaction.setDetails("transaction made in Manabí.");
        transaction.setDate(new Date());
        transaction.setValue(new BigDecimal(50));
        transaction.setStatus("ACTIVE");
        transaction.setAccount(account);
        transaction.setTypeTransaction(typeTransaction);

        TypeTransactionResponseDTO typeTransactionResponseDTO = new TypeTransactionResponseDTO("Deposit from branch",
                "Deposits made from a branch.", new BigDecimal(0), true, false, "ACTIVE");

        transactionRequestDTO = new TransactionRequestDTO(new BigDecimal(100), new Date(),
                "2200000000", "transaction made in Manabí.", "ACTIVE", "test");
        transactionResponseDTO = new TransactionResponseDTO("2200000000", "", new BigDecimal(100), new Date(),
                "ACTIVE", Mono.just(accountResponseDTO), Mono.just(typeTransactionResponseDTO));
    }

    @Test
    void createTransaction_ShouldReturnTransactionResponseDTO() {
        when(transactionMapper.mapToEntity(transactionRequestDTO)).thenReturn(transaction);
        when(validateTransaction.validateTransaction(transaction)).thenReturn(Mono.just(transaction));
        when(balanceCalculator.calculate(any(), any())).thenReturn(new BigDecimal("1000"));
        when(accountRepository.save(transaction.getAccount())).thenReturn(Mono.just(transaction.getAccount()));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));
        when(transactionMapper.mapToDTO(transaction)).thenReturn(transactionResponseDTO);

        Mono<TransactionResponseDTO> result = transactionService.createTransaction(transactionRequestDTO);

        StepVerifier.create(result)
                .expectNext(transactionResponseDTO)
                .expectComplete()
                .verify();

        verify(transactionMapper, times(1)).mapToEntity(transactionRequestDTO);
        verify(validateTransaction, times(1)).validateTransaction(transaction);
        verify(balanceCalculator, times(1)).calculate(any(), any());
        verify(accountRepository, times(1)).save(transaction.getAccount());
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).mapToDTO(transaction);
    }

    @Test
    public void testGetAllTransaction_WhenTransactionsExist() {
        when(transactionRepository.findAll()).thenReturn(Flux.just(transaction));
        when(transactionMapper.mapToDTO(transaction)).thenReturn(transactionResponseDTO);

        Flux<TransactionResponseDTO> result = transactionService.getAllTransactions();

        StepVerifier.create(result)
                .expectNext(transactionResponseDTO)
                .verifyComplete();

        verify(transactionRepository).findAll();
        verify(transactionMapper).mapToDTO(transaction);
    }

    @Test
    public void testGetAllTransactions_WhenNoTransactionsExist() {
        when(transactionRepository.findAll()).thenReturn(Flux.empty());

        Flux<TransactionResponseDTO> result = transactionService.getAllTransactions();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof EmptyCollectionException &&
                        throwable.getMessage().equals("No transactions records found."))
                .verify();

        verify(transactionRepository).findAll();
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void updateBalanceAndSave_ShouldUpdateAccountBalanceAndSaveTransaction() {
        BigDecimal newBalance = new BigDecimal("900");
        when(balanceCalculator.calculate(transaction, account.getAvailableBalance())).thenReturn(newBalance);
        when(accountRepository.save(account)).thenReturn(Mono.just(account));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        Mono<Transaction> result = transactionService.updateBalanceAndSave(transaction);

        StepVerifier.create(result)
                .expectNext(transaction)
                .expectComplete()
                .verify();

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(transaction);
        assertEquals(newBalance, account.getAvailableBalance());
    }
}