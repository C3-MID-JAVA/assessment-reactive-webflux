package ec.com.example.bank_account.util.process;

import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.entity.Transaction;
import ec.com.example.bank_account.entity.TypeTransaction;
import ec.com.example.bank_account.exception.RecordNotFoundException;
import ec.com.example.bank_account.exception.TransactionRejectedException;
import ec.com.example.bank_account.repository.AccountRepository;
import ec.com.example.bank_account.repository.TypeTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidateTransactionImpTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TypeTransactionRepository typeTransactionRepository;

    @InjectMocks
    private ValidateTransactionImp validateTransactionImp;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setAccountNumber("12345");
        transaction.setTypeTransactionIdentify("T001");
        transaction.setValue(new BigDecimal("100"));
    }

    @Test
    public void testValidateTransaction_Success() {
        Account account = new Account();
        account.setNumber("12345");
        account.setStatus("ACTIVE");
        account.setAvailableBalance(new BigDecimal("500"));

        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setDiscount(false);
        typeTransaction.setValue(new BigDecimal("10"));

        when(accountRepository.findByNumber("12345")).thenReturn(Mono.just(account));
        when(typeTransactionRepository.findById("T001")).thenReturn(Mono.just(typeTransaction));

        StepVerifier.create(validateTransactionImp.validateTransaction(transaction))
                .expectNext(transaction)
                .verifyComplete();

        verify(accountRepository).findByNumber("12345");
        verify(typeTransactionRepository).findById("T001");
    }

    @Test
    public void testValidateTransaction_AccountNotFound() {
        when(accountRepository.findByNumber("12345")).thenReturn(Mono.empty());
        when(typeTransactionRepository.findById("T001")).thenReturn(Mono.just(new TypeTransaction()));

        StepVerifier.create(validateTransactionImp.validateTransaction(transaction))
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(accountRepository).findByNumber("12345");
        verify(typeTransactionRepository).findById("T001");
    }

    @Test
    public void testValidateTransaction_TypeTransactionNotFound() {
        Account account = new Account();
        account.setNumber("12345");
        account.setStatus("ACTIVE");
        account.setAvailableBalance(new BigDecimal("500"));

        when(accountRepository.findByNumber("12345")).thenReturn(Mono.just(account));
        when(typeTransactionRepository.findById("T001")).thenReturn(Mono.empty());

        StepVerifier.create(validateTransactionImp.validateTransaction(transaction))
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(accountRepository).findByNumber("12345");
        verify(typeTransactionRepository).findById("T001");
    }

    @Test
    public void testValidateTransaction_AccountInactive() {
        Account account = new Account();
        account.setNumber("12345");
        account.setStatus("INACTIVE");
        account.setAvailableBalance(new BigDecimal("500"));

        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setDiscount(false);
        typeTransaction.setValue(new BigDecimal("10"));

        when(accountRepository.findByNumber("12345")).thenReturn(Mono.just(account));
        when(typeTransactionRepository.findById("T001")).thenReturn(Mono.just(typeTransaction));

        StepVerifier.create(validateTransactionImp.validateTransaction(transaction))
                .expectError(TransactionRejectedException.class)
                .verify();

        verify(accountRepository).findByNumber("12345");
        verify(typeTransactionRepository).findById("T001");
    }

    @Test
    public void testValidateTransaction_InsufficientFunds() {
        Account account = new Account();
        account.setNumber("12345");
        account.setStatus("ACTIVE");
        account.setAvailableBalance(new BigDecimal("50"));

        TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setDiscount(true);
        typeTransaction.setValue(new BigDecimal("10"));

        when(accountRepository.findByNumber("12345")).thenReturn(Mono.just(account));
        when(typeTransactionRepository.findById("T001")).thenReturn(Mono.just(typeTransaction));

        StepVerifier.create(validateTransactionImp.validateTransaction(transaction))
                .expectError(TransactionRejectedException.class)
                .verify();

        verify(accountRepository).findByNumber("12345");
        verify(typeTransactionRepository).findById("T001");
    }
}