package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.AccountInDTO;
import com.reactivo.banco.model.entity.Account;
import com.reactivo.banco.model.entity.Client;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


import java.math.BigDecimal;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

public class AccountImplServiceTest {

    @Mock
    private AccountRepository cuentaRepository;

    @Mock
    private ClientRepository clienteRepository;

    private AccountImplService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountImplService(cuentaRepository, clienteRepository);
    }

    @Test
    public void testCreateAccountSuccess() {
        AccountInDTO accountInDTO = new AccountInDTO("12345", new BigDecimal("1000.00"), "1", "card123");
        Client client = new Client("1", "ID123", "John", "Doe", "john@example.com", "123456789", "Some Address", LocalDate.of(1990, 1, 1));

        when(clienteRepository.findById("1")).thenReturn(Mono.just(client));
        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(new Account("1", "12345", new BigDecimal("1000.00"), "1", "card123")));

        StepVerifier.create(accountService.createAccount(accountInDTO))
                .expectNextMatches(accountOutDTO -> accountOutDTO.getAccountNumber().equals("12345"))
                .verifyComplete();
    }

    @Test
    public void testCreateAccountClientNotFound() {
        AccountInDTO accountInDTO = new AccountInDTO("12345", new BigDecimal("1000.00"), "1", "card123");

        when(clienteRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(accountService.createAccount(accountInDTO))
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("Cliente no encontrado con ID: 1"))
                .verify();
    }

    @Test
    public void testGetAllAccounts() {
        Account account = new Account("1", "12345", new BigDecimal("1000.00"), "1", "card123");
        when(cuentaRepository.findAll()).thenReturn(Flux.just(account));

        StepVerifier.create(accountService.getAllAccounts())
                .expectNextMatches(accountOutDTO -> accountOutDTO.getAccountNumber().equals("12345"))
                .verifyComplete();
    }

    @Test
    public void testGetAllAccountsNoAccounts() {
        when(cuentaRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(accountService.getAllAccounts())
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("No existen cuentas registradas."))
                .verify();
    }

    @Test
    public void testGetAccountByIdSuccess() {
        Account account = new Account("1", "12345", new BigDecimal("1000.00"), "1", "card123");
        when(cuentaRepository.findById("1")).thenReturn(Mono.just(account));

        StepVerifier.create(accountService.getAccountById("1"))
                .expectNextMatches(accountOutDTO -> accountOutDTO.getAccountNumber().equals("12345"))
                .verifyComplete();
    }

    @Test
    public void testGetAccountByIdNotFound() {
        when(cuentaRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(accountService.getAccountById("1"))
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException && throwable.getMessage().equals("Cuenta no encontrada con ID: 1"))
                .verify();
    }

    @Test
    public void testUpdateAccountSuccess() {
        AccountInDTO accountInDTO = new AccountInDTO("54321", new BigDecimal("2000.00"), "1", "card123");
        Account existingAccount = new Account("1", "12345", new BigDecimal("1000.00"), "1", "card123");

        when(cuentaRepository.findById("1")).thenReturn(Mono.just(existingAccount));
        when(cuentaRepository.save(any(Account.class))).thenReturn(Mono.just(existingAccount));

        StepVerifier.create(accountService.updateAccount("1", accountInDTO))
                .expectNextMatches(accountOutDTO -> accountOutDTO.getAccountNumber().equals("54321") && accountOutDTO.getBalance().equals(new BigDecimal("2000.00")))
                .verifyComplete();
    }

    @Test
    public void testDeleteAccountSuccess() {
        Account account = new Account("1", "12345", new BigDecimal("1000.00"), "1", "card123");

        when(cuentaRepository.findById("1")).thenReturn(Mono.just(account));
        when(cuentaRepository.delete(account)).thenReturn(Mono.empty());

        StepVerifier.create(accountService.deleteAccount("1"))
                .verifyComplete();
    }
}
