package org.bankAccountManager.service;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Transaction;
import org.bankAccountManager.repository.AccountRepository;
import org.bankAccountManager.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    /*@Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImplementation transactionService;

    private Transaction transaction;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sourceAccount = new Account(1, "Source Account", new BigDecimal("1000.00"));
        destinationAccount = new Account(2, "Destination Account", new BigDecimal("500.00"));
        transaction = new Transaction(1, "branch_transfer", new BigDecimal("100.00"), sourceAccount, destinationAccount, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testCreateTransaction_success_branch_transfer() {
        // Arrange
        when(transactionRepository.existsById(transaction.getId())).thenReturn(false);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount).thenReturn(destinationAccount);

        // Act
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Assert
        assertNotNull(createdTransaction);
        assertEquals(transaction.getId(), createdTransaction.getId());
        assertEquals(transaction.getType(), createdTransaction.getType());
        verify(transactionRepository, times(1)).save(transaction);
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void testCreateTransaction_insufficient_balance() {
        // Arrange
        Transaction invalidTransaction = new Transaction(2, "atm_withdrawal", new BigDecimal("2000.00"), sourceAccount, destinationAccount, new Timestamp(System.currentTimeMillis()));
        when(transactionRepository.existsById(invalidTransaction.getId())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(invalidTransaction);
        });
        assertEquals("Insufficient balance in account: 1", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateTransaction_invalid_transaction_type() {
        // Arrange
        Transaction invalidTransaction = new Transaction(3, "invalid_type", new BigDecimal("100.00"), sourceAccount, destinationAccount, new Timestamp(System.currentTimeMillis()));
        when(transactionRepository.existsById(invalidTransaction.getId())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(invalidTransaction);
        });
        assertEquals("Invalid transaction type: invalid_type", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testGetTransactionById_success() {
        // Arrange
        when(transactionRepository.findTransactionById(transaction.getId())).thenReturn(transaction);

        // Act
        Transaction foundTransaction = transactionService.getTransactionById(transaction.getId());

        // Assert
        assertNotNull(foundTransaction);
        assertEquals(transaction.getId(), foundTransaction.getId());
        verify(transactionRepository, times(1)).findTransactionById(transaction.getId());
    }

    @Test
    void testGetTransactionById_notFound() {
        // Arrange
        when(transactionRepository.findTransactionById(transaction.getId())).thenReturn(null);

        // Act
        Transaction foundTransaction = transactionService.getTransactionById(transaction.getId());

        // Assert
        assertNull(foundTransaction);
        verify(transactionRepository, times(1)).findTransactionById(transaction.getId());
    }

    @Test
    void testGetTransactionsByBranchId() {
        // Arrange
        when(transactionRepository.findTransactionsByBranchId(1)).thenReturn(List.of(transaction));

        // Act
        List<Transaction> transactions = transactionService.getTransactionsByBranchId(1);

        // Assert
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        verify(transactionRepository, times(1)).findTransactionsByBranchId(1);
    }

    @Test
    void testGetTransactionsBySourceAccount() {
        // Arrange
        when(transactionRepository.findTransactionsBySourceAccount(sourceAccount.getId())).thenReturn(List.of(transaction));

        // Act
        List<Transaction> transactions = transactionService.getTransactionsBySourceAccountId(sourceAccount.getId());

        // Assert
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        verify(transactionRepository, times(1)).findTransactionsBySourceAccount(sourceAccount.getId());
    }

    @Test
    void testUpdateTransaction() {
        // Arrange
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);

        // Assert
        assertNotNull(updatedTransaction);
        assertEquals(transaction.getId(), updatedTransaction.getId());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDeleteTransaction() {
        // Act
        transactionService.deleteTransaction(transaction);

        // Assert
        verify(transactionRepository, times(1)).delete(transaction);
    }*/
}
