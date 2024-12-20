package com.sofkau.usrv_accounts_manager.services;

import com.sofkau.usrv_accounts_manager.dto.AccountDTO;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.repository.AccountRepository;
import com.sofkau.usrv_accounts_manager.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountModel account;
    private AccountDTO accountDTO;


    @BeforeEach
    void setUp() {

        account = new AccountModel();
        account.setAccountNumber("1234599");
        accountDTO = new AccountDTO(new ArrayList<>(),"1234599",
                BigDecimal.valueOf(100), "DEBIT", "My cas", new ArrayList<>());

    }

    @Test
    @DisplayName("Should create an account when account does not already exist")
    void createAccount_Success() {
        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber())).thenReturn(Mono.empty());
        when(accountRepository.save(any(AccountModel.class))).thenReturn(Mono.just(account));


        StepVerifier.create(accountService.createAccount(accountDTO))
                .assertNext(accountDTO -> assertEquals(accountDTO.getAccountNumber(), account.getAccountNumber()))
                .verifyComplete();


    }

    @Test
    @DisplayName("Should throw exception when account already exists")
    void createAccount_AccountAlreadyExists() {

        when(accountRepository.findByAccountNumber(accountDTO.getAccountNumber())).thenReturn(Mono.just(account));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(Mono.just(account));


        StepVerifier.create(accountService.createAccount(accountDTO))
                .expectErrorMatches(err  -> err.getMessage().equals("Account already exists"))
                .verify();

    }
}