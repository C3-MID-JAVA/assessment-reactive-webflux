package edisonrmedina.CityBank.service;

import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.entity.transaction.Transaction;
import edisonrmedina.CityBank.entity.transaction.TransactionCostStrategy.*;
import edisonrmedina.CityBank.enums.TransactionType;
import edisonrmedina.CityBank.repository.TransactionRepository;
import edisonrmedina.CityBank.service.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    private Map<TransactionType, TransactionCostStrategy> costStrategies;

    private BankAccount bankAccount;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        costStrategies = Map.of(
                TransactionType.DEPOSIT_BRANCH, new DepositBranchCostStrategy(),
                TransactionType.DEPOSIT_ATM, new DepositAtmCostStrategy(),
                TransactionType.DEPOSIT_ACCOUNT, new DepositAccountCostStrategy(),
                TransactionType.PURCHASE_PHYSICAL, new PurchasePhysicalCostStrategy(),
                TransactionType.PURCHASE_ONLINE, new PurchaseWebCostStrategy(),
                TransactionType.WITHDRAW_ATM, new WithdrawAtmCostStrategy()
        );

        bankAccount = BankAccount.builder()
                .id("675e962784c89c424d3bc7b6")
                .balance(new BigDecimal("1000"))
                .build();

        transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW_ATM)
                .amount(new BigDecimal("200"))
                .timestamp(LocalDateTime.now())
                .bankAccount(bankAccount)
                .build();
    }

    @Test
    void testCreateTransaction_typeNotSupported() {
        transaction.setType(TransactionType.DEPOSIT_OUT); // Tipo no soportado

        StepVerifier.create(transactionsService.calculateNewBalance(transaction, costStrategies))
                .expectError(IllegalArgumentException.class)
                .verify();

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testCreateTransaction_successfulWithdrawal() {
        // Simular repositorio
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionsService.calculateNewBalance(transaction, costStrategies))
                .expectNextMatches(updatedTransaction -> {
                    Assertions.assertEquals(new BigDecimal("1"), updatedTransaction );
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testCreateTransaction_insufficientBalance() {
        // Configurar saldo insuficiente
        bankAccount.setBalance(new BigDecimal("100"));

        StepVerifier.create(transactionsService.calculateNewBalance(transaction, costStrategies))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Saldo insuficiente para realizar la transacción"))
                .verify();
    }

}
