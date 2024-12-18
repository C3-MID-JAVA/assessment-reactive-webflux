package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.exception.NotFoundException;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.repository.AccountRepository;
import com.bankmanagement.bankmanagement.repository.UserRepository;
import com.bankmanagement.bankmanagement.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    // Positive
    @Test
    void shouldCreateAccountSuccessfully(){
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("675e0e1259d6de4eda5b29b7");
        Account account = new Account("675e0e4a59d6de4eda5b29b8", "302638f2", 0.0, accountRequestDTO.getUserId());

        User mockUser = new User();
        mockUser.setId(accountRequestDTO.getUserId());

        when(userRepository.findById(accountRequestDTO.getUserId())).thenReturn(Optional.of(mockUser));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDTO response = accountService.create(accountRequestDTO);
        assertNotNull(response);
        assertEquals(accountRequestDTO.getUserId(), response.getUserId());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(userRepository, times(1)).findById(accountRequestDTO.getUserId());
    }

    //Negative
    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringAccountCreation() {
        String invalidUserId = "675e0e1259d6de4eda5b29b5";
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO(invalidUserId);

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            accountService.create(accountRequestDTO);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(invalidUserId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldReturnAccountsListForUser() {
        String userId = "675e0e1259d6de4eda5b29b6";

        List<Account> accounts = List.of(
                new Account("675e0e4a59d6de4eda5b29b8", "12345678", 100.0, userId),
                new Account("675e0e4a59d6de4eda5b29b9", "87654321", 200.0, userId)
        );

        when(accountRepository.findByUserId(userId)).thenReturn(accounts);

        List<AccountResponseDTO> response = accountService.getAllByUserId(userId);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("12345678", response.get(0).getAccountNumber());
        assertEquals("87654321", response.get(1).getAccountNumber());

        verify(accountRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldFindAccountByAccountNumberSuccessfully() {
        String accountNumber = "12345678";
        Account account = new Account(
                "675e0e4a59d6de4eda5b29b8",
                accountNumber,
                100.0,
                "675e0e1259d6de4eda5b29b7");

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        AccountResponseDTO response = accountService.findByAccountNumber(accountNumber);

        assertNotNull(response);
        assertEquals(accountNumber, response.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByAccountNumber() {
        String accountNumber = "675e0e4a59d6de4eda5b29b3";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            accountService.findByAccountNumber(accountNumber);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }
}
