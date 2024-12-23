package com.example.bank.service;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import com.example.bank.entity.Account;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    void accountCreatedOK () {
        AccountRequestDTO newAccount = new AccountRequestDTO("nat", BigDecimal.valueOf(0));
        String accountId = newAccount.getId();

        Mono<Account> account = Mono.just(new Account(accountId, "", BigDecimal.valueOf(0), new ArrayList<>()));

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Mono<AccountResponseDTO> response = accountService.createAccount(newAccount);

        assertNotNull(response);
        assertEquals(accountId, response.block().getId());
    }

    @Test
    void getAllAccountsOK () {
        List<Account> accounts = List.of(
                new Account("1", "holder1", BigDecimal.valueOf(0), new ArrayList<>()),
                new Account("2", "holder2", BigDecimal.valueOf(0), new ArrayList<>())
        );

        when(accountRepository.findAll()).thenReturn(Flux.fromIterable(accounts));

        var response = accountService.getAllAccounts();

        assertNotNull(response);
        assertEquals(2, response.count().block());
        assertEquals("1", response.elementAt(0).block().getId());
        assertEquals("2", response.elementAt(1).block().getId());
    }

    @Test
    void getAllAccountsFail () {
        List<Account> accounts = new ArrayList<>();

        when(accountRepository.findAll()).thenReturn(Flux.fromIterable(accounts));

        var response = accountService.getAllAccounts();

        StepVerifier.create(response).expectErrorMessage("No accounts found.").verify();
    }

    @Test
    void getAccountByIdOK () {
        String accountId = "1";
        Account account = new Account("1", "holder1", BigDecimal.valueOf(0), new ArrayList<>());

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));

        var response = accountService.getAccountById(accountId);

        assertNotNull(response);
        assertEquals(accountId, response.block().getId());
    }

    @Test
    void getAccountByIdFail () {
        String accountId = "2";

        when(accountRepository.findById(accountId)).thenReturn(Mono.empty());

        var response = accountService.getAccountById(accountId);

        StepVerifier.create(response).expectErrorMessage("Account not found.").verify();
    }
}