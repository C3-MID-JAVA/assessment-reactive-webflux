package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    /*@Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImplementation accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account(1, "123456789", "savings", 1000.0);
    }

    @Test
    void testCreateAccount_success() {
        // Arrange
        when(accountRepository.existsById(account.getId())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        Account createdAccount = accountService.createAccount(account);

        // Assert
        assertNotNull(createdAccount);
        assertEquals(account.getId(), createdAccount.getId());
        assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccount_accountExists_throwsException() {
        // Arrange
        when(accountRepository.existsById(account.getId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(account);
        });

        assertEquals("Account already exists", exception.getMessage());
        verify(accountRepository, never()).save(account);
    }

    @Test
    void testGetAccountById_success() {
        // Arrange
        when(accountRepository.findAccountById(account.getId())).thenReturn(account);

        // Act
        Account foundAccount = accountService.getAccountById(account.getId());

        // Assert
        assertNotNull(foundAccount);
        assertEquals(account.getId(), foundAccount.getId());
        verify(accountRepository, times(1)).findAccountById(account.getId());
    }

    @Test
    void testGetAccountById_notFound() {
        // Arrange
        when(accountRepository.findAccountById(account.getId())).thenReturn(null);

        // Act
        Account foundAccount = accountService.getAccountById(account.getId());

        // Assert
        assertNull(foundAccount);
        verify(accountRepository, times(1)).findAccountById(account.getId());
    }

    @Test
    void testGetAccountByCustomerId_success() {
        // Arrange
        when(accountRepository.findAccountByCustomerId(account.getId())).thenReturn(account);

        // Act
        Account foundAccount = accountService.getAccountByCustomerId(account.getId());

        // Assert
        assertNotNull(foundAccount);
        assertEquals(account.getId(), foundAccount.getId());
        verify(accountRepository, times(1)).findAccountByCustomerId(account.getId());
    }

    @Test
    void testGetAllAccounts() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(List.of(account));

        // Act
        List<Account> accounts = accountService.getAllAccounts();

        // Assert
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAccount() {
        // Arrange
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account updatedAccount = accountService.updateAccount(account);

        // Assert
        assertNotNull(updatedAccount);
        assertEquals(account.getId(), updatedAccount.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeleteAccount() {
        // Act
        accountService.deleteAccount(account);

        // Assert
        verify(accountRepository, times(1)).delete(account);
    }*/
}
