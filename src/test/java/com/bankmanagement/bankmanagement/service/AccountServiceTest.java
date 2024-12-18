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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void shouldCreateAccountSuccessfully() {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("675e0e1259d6de4eda5b29b7");
        Account account = new Account("675e0e4a59d6de4eda5b29b8", "302638f2", 0.0, accountRequestDTO.getUserId());

        User mockUser = new User();
        mockUser.setId(accountRequestDTO.getUserId());

        when(userRepository.findById(accountRequestDTO.getUserId())).thenReturn(Mono.just(mockUser));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(account));

        Mono<AccountResponseDTO> response = accountService.create(accountRequestDTO);

        StepVerifier.create(response)
                .assertNext(accountResponseDTO -> {
                    assertNotNull(accountResponseDTO);
                    assertEquals(accountRequestDTO.getUserId(), accountResponseDTO.getUserId());
                })
                .verifyComplete();

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(userRepository, times(1)).findById(accountRequestDTO.getUserId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringAccountCreation() {
        String invalidUserId = "675e0e1259d6de4eda5b29b5";
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO(invalidUserId);

        when(userRepository.findById(invalidUserId)).thenReturn(Mono.empty());

        Mono<AccountResponseDTO> response = accountService.create(accountRequestDTO);

        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("User not found"))
                .verify();

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

        when(accountRepository.findByUserId(userId)).thenReturn(Flux.fromIterable(accounts));

        Flux<AccountResponseDTO> response = accountService.getAllByUserId(userId);

        StepVerifier.create(response)
                .expectNextMatches(accountResponseDTO -> "12345678".equals(accountResponseDTO.getAccountNumber()))
                .expectNextMatches(accountResponseDTO -> "87654321".equals(accountResponseDTO.getAccountNumber()))
                .verifyComplete();

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

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.just(account));

        Mono<AccountResponseDTO> response = accountService.findByAccountNumber(accountNumber);

        StepVerifier.create(response)
                .assertNext(accountResponseDTO -> {
                    assertNotNull(accountResponseDTO);
                    assertEquals(accountNumber, accountResponseDTO.getAccountNumber());
                })
                .verifyComplete();

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByAccountNumber() {
        String accountNumber = "675e0e4a59d6de4eda5b29b3";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

        Mono<AccountResponseDTO> response = accountService.findByAccountNumber(accountNumber);

        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("Account not found"))
                .verify();

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }
}
