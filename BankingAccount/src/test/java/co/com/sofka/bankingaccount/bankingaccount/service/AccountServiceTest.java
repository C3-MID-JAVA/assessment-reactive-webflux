package co.com.sofka.bankingaccount.bankingaccount.service;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.repository.AccountRepository;
import co.com.sofka.bankingaccount.bankingaccount.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.error;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private BankAccount mockAccount;
    private TransactionBase mockTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockAccount = new BankAccount();
        mockAccount.setId(UUID.randomUUID().toString());
        mockAccount.setBalance(BigDecimal.valueOf(1000));
        mockAccount.setEntitledUser("Test User");

        mockTransaction = new TransactionBase() {
            @Override
            public BigDecimal calculateImpact() {
                return BigDecimal.valueOf(-200);
            }
        };
        mockTransaction.setId(UUID.randomUUID().toString());
        mockTransaction.setAccountId(mockAccount.getId());
        mockTransaction.setAmount(BigDecimal.valueOf(-200));
        mockTransaction.setCost(BigDecimal.valueOf(5));
        mockTransaction.setType("WITHDRAW");
    }

    @Test
    void testCreateAccount() {
        // Configurar datos simulados
        CreateAccountRequestDTO requestDTO = new CreateAccountRequestDTO();
        requestDTO.setBalance(BigDecimal.valueOf(500));
        requestDTO.setEntitledUserName("John Doe");

        BankAccount savedAccount = new BankAccount();
        savedAccount.setId(UUID.randomUUID().toString());
        savedAccount.setBalance(requestDTO.getBalance());
        savedAccount.setEntitledUser(requestDTO.getEntitledUserName());

        when(accountRepository.save(any(BankAccount.class))).thenReturn(Mono.just(savedAccount));

        // Ejecutar y verificar
        StepVerifier.create(accountService.createAccount(requestDTO))
                .expectNext(savedAccount)
                .verifyComplete();

        verify(accountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() {
        when(accountRepository.findById(mockAccount.getId())).thenReturn(Mono.just(mockAccount));

        StepVerifier.create(accountService.getAccount(mockAccount.getId()))
                .expectNextMatches(account -> account.getId().equals(mockAccount.getId()))
                .verifyComplete();

        verify(accountRepository, times(1)).findById(mockAccount.getId());
    }

    @Test
    void getAccount_ShouldThrowError_WhenAccountDoesNotExist() {
        when(accountRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(accountService.getAccount("invalid-id"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Account not found!"))
                .verify();

        verify(accountRepository, times(1)).findById("invalid-id");
    }

    @Test
    void executeTransaction_ShouldUpdateBalance() {
        when(accountRepository.findById(mockAccount.getId())).thenReturn(Mono.just(mockAccount));
        when(transactionRepository.save(any(TransactionBase.class))).thenReturn(Mono.just(mockTransaction));
        when(accountRepository.save(any(BankAccount.class))).thenReturn(Mono.just(mockAccount));

        StepVerifier.create(accountService.executeTransaction(mockAccount.getId(), mockTransaction))
                .expectNextMatches(balance -> balance.compareTo(BigDecimal.valueOf(800)) == 0)
                .verifyComplete();

        verify(accountRepository, times(1)).findById(mockAccount.getId());
        verify(transactionRepository, times(1)).save(any(TransactionBase.class));
    }
}
