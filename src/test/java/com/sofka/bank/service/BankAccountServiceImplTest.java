package com.sofka.bank.service;

import com.sofka.bank.dto.BankAccountDTO;
import com.sofka.bank.entity.BankAccount;
import com.sofka.bank.repository.BankAccountRepository;
import com.sofka.bank.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private BankAccountDTO validBankAccountDTO;

    @BeforeEach
    public void setup() {
        validBankAccountDTO = new BankAccountDTO(null, "1000008","John Doe", 1000.0,new ArrayList<>());
    }

    @Test
    @DisplayName("Should successfully create account with provided data")
    public void testCreateAccount_Success() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(validBankAccountDTO.getAccountHolder());
        bankAccount.setGlobalBalance(validBankAccountDTO.getGlobalBalance());
        bankAccount.setAccountNumber(validBankAccountDTO.getAccountNumber());
        bankAccount.setId("randomGeneratedId");
        bankAccount.setTransactions(new ArrayList<>());

        Mockito.when(bankAccountRepository.existsByAccountNumber(validBankAccountDTO.getAccountNumber())).thenReturn(Mono.just(false));
        Mockito.when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(Mono.just(bankAccount));

        Mono<BankAccountDTO> createdAccountDTO = bankAccountService.createAccount(validBankAccountDTO);

        StepVerifier.create(createdAccountDTO)
                .assertNext(dto -> {
                    assertEquals("John Doe", dto.getAccountHolder());
                    assertEquals(1000.0, dto.getGlobalBalance());
                    assertEquals("1000008", dto.getAccountNumber());
                    assertNotNull(dto.getId());
                })

                .verifyComplete();
    }

    @Test
    @DisplayName("Should return an error message when account holder field is null")
    public void testCreateAccount_AccountHolderIsNull() {
        validBankAccountDTO.setAccountHolder(null);

        Mono<BankAccountDTO> result = bankAccountService.createAccount(validBankAccountDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Account holder is required"))
                .verify();
    }

    @Test
    @DisplayName("Should return an error message when global balance is a negative number")
    public void testCreateAccount_InvalidBalance() {
        validBankAccountDTO.setGlobalBalance(-500.0);

        Mono<BankAccountDTO> result = bankAccountService.createAccount(validBankAccountDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Global balance must be a positive number"))
                .verify();
    }

    @Test
    @DisplayName("Should return an error message when account number is not unique")
    public void testCreateAccount_AccountNumberNotUnique() {
        Mockito.when(bankAccountRepository.existsByAccountNumber(validBankAccountDTO.getAccountNumber())).thenReturn(Mono.just(true));

        Mono<BankAccountDTO> result = bankAccountService.createAccount(validBankAccountDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Account number already exists"))
                .verify();
    }

    @Test
    @DisplayName("Should return all accounts with data matching the database")
    public void testGetAllAccounts_Success() {
        BankAccount bankAccount1 = new BankAccount("12345", "1000008","John Doe", 1000.0,new ArrayList<>());
        BankAccount bankAccount2 = new BankAccount("67890", "1000009", "Jane Doe", 2000.0, new ArrayList<>());
        List<BankAccount> bankAccounts = Arrays.asList(bankAccount1, bankAccount2);

        Mockito.when(bankAccountRepository.findAll()).thenReturn(Flux.fromIterable(bankAccounts));
        Flux<BankAccountDTO> accountsDTO = bankAccountService.getAllAccounts();

        StepVerifier.create(accountsDTO)
                .expectNextMatches(account -> account.getAccountHolder().equals("John Doe"))
                .expectNextMatches(account -> account.getAccountHolder().equals("Jane Doe"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return an empty list when no accounts are found")
    public void testGetAllAccounts_Empty() {
        Mockito.when(bankAccountRepository.findAll()).thenReturn(Flux.empty());

        Flux<BankAccountDTO> accountsDTO = bankAccountService.getAllAccounts();

        StepVerifier.create(accountsDTO)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Should assert true when the unique account number equals false. If assertion is false, then " +
            "account number is unique")
    public void testIsAccountNumberUnique_True() {
        Mockito.when(bankAccountRepository.existsByAccountNumber("12345")).thenReturn(Mono.just(false));

        Mono<Boolean> result = bankAccountService.isAccountNumberUnique("12345");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should assert false when the unique account number equals true (already exists)")
    public void testIsAccountNumberUnique_False() {
        Mockito.when(bankAccountRepository.existsByAccountNumber("12345")).thenReturn(Mono.just(true));

        Mono<Boolean> result = bankAccountService.isAccountNumberUnique("12345");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

}


