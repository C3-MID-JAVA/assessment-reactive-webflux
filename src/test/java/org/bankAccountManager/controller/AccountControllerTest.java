package org.bankAccountManager.controller;

import org.bankAccountManager.DTO.request.AccountRequestDTO;
import org.bankAccountManager.DTO.request.BranchRequestDTO;
import org.bankAccountManager.DTO.request.CardRequestDTO;
import org.bankAccountManager.DTO.request.TransactionRequestDTO;
import org.bankAccountManager.DTO.response.AccountResponseDTO;
import org.bankAccountManager.DTO.response.BranchResponseDTO;
import org.bankAccountManager.DTO.response.CardResponseDTO;
import org.bankAccountManager.DTO.response.TransactionResponseDTO;
import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.entity.Card;
import org.bankAccountManager.entity.Transaction;
import org.bankAccountManager.service.implementations.AccountServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.bankAccountManager.mapper.DTORequestMapper.toAccount;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@WebFluxTest(controllers = AccountController.class)
//@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class AccountControllerTest {

    /*@Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountServiceImplementation accountService;

    private AccountRequestDTO accountRequestDTO;
    private AccountResponseDTO accountResponseDTO;
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        CardRequestDTO cardRequestDTO = new CardRequestDTO(1,
                "1234567890123456",
                "debit",
                LocalDateTime.now(),
                "123");
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(1,
                List.of(new BranchRequestDTO(1,
                        "Pepe",
                        "aaaaa",
                        "123456"
                )),
                LocalDateTime.now(),
                "branch_transfer",
                new BigDecimal("100"),
                "desc",
                new AccountRequestDTO());
        accountRequestDTO = new AccountRequestDTO(1234,
                "1234567890",
                "savings",
                new BigDecimal("1000"),
                List.of(cardRequestDTO),
                List.of(transactionRequestDTO));
        CardResponseDTO cardResponseDTO = new CardResponseDTO(1,
                "1234567890123456",
                "debit",
                LocalDateTime.now(),
                "123");
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(
                1,
                List.of(new BranchResponseDTO(1,
                        "Pepe",
                        "aaaaa",
                        "123456"
                )),
                LocalDateTime.now(),
                "branch_transfer",
                new BigDecimal("100"),
                "desc",
                new AccountResponseDTO()
        );
        accountResponseDTO = new AccountResponseDTO(1234,
                "1234567890",
                "savings",
                new BigDecimal("1000"),
                List.of(cardResponseDTO),
                List.of(transactionResponseDTO));
        Card card = new Card(1,
                "1234567890123456",
                "debit",
                LocalDateTime.now(),
                "123");
        Transaction transaction = new Transaction(
                1,
                List.of(new Branch(1,
                        "Pepe",
                        "aaaaa",
                        "123456"
                )),
                LocalDateTime.now(),
                "branch_transfer",
                new BigDecimal("100"),
                "desc",
                new Account()
        );
        account = new Account(1234567891,
                "1234567890",
                "savings",
                new BigDecimal("1000"),
                List.of(card),
                List.of(transaction));
    }

    // Test positive case for createAccount
    @Test
    public void createAccount_ShouldReturnCreatedAccount() {
        when(accountService.createAccount(Mono.just(account).thenReturn(account))
                .thenReturn(Mono.just(accountResponseDTO)));
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountResponseDTO.class)
                .isEqualTo(accountResponseDTO);
    }

    // Test negative case for createAccount when account already exists
    @Test
    public void createAccount_AlreadyExists_ShouldReturnBadRequest() {
        when(accountService.createAccount(toAccount(Mono.just(any(AccountRequestDTO.class))))
                .thenReturn(Mono.error(new IllegalArgumentException("Account already exists"))));
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Account already exists");
    }

    // Test positive case for getAccountById
    @Test
    public void getAccountById_ShouldReturnAccount() {
        when(accountService.getAccountById(Mono.just(account.getId()))
                .thenReturn(Mono.just(accountResponseDTO)));
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/accounts/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isFound()
                .expectBody(AccountResponseDTO.class)
                .isEqualTo(accountResponseDTO);
    }

    // Test negative case for getAccountById when account is not found
    @Test
    public void getAccountById_AccountNotFound_ShouldReturnNotFound() {
        when(accountService.getAccountById(Mono.just(account.getId())))
                .thenReturn(Mono.empty());
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/accounts/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Account not found");
    }

    // Test positive case for getAllAccounts
    @Test
    public void getAllAccounts_ShouldReturnAccounts() {
        when(accountService.getAllAccounts())
                .thenReturn(Flux.just());
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get().uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponseDTO.class)
                .contains(accountResponseDTO);
    }

    // Test positive case for updateAccount
    @Test
    public void updateAccount_ShouldReturnUpdatedAccount() {
        when(accountService.updateAccount(Mono.just(account))
                .thenReturn(Mono.just(accountResponseDTO)));
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponseDTO.class)
                .isEqualTo(accountResponseDTO);
    }

    // Test negative case for updateAccount when account is not found
    @Test
    public void updateAccount_AccountNotFound_ShouldReturnNotFound() {
        if (account != null)
            when(accountService.updateAccount(Mono.just(account)))
                    .thenReturn(Mono.error(new IllegalArgumentException("Account already exists")));
        else
            System.out.println("Account not found");
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountRequestDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Account not found");
    }

    // Test positive case for deleteAccount
    @Test
    public void deleteAccount_ShouldReturnNoContent() {
        when(accountService.deleteAccount(Mono.just(account.getId())))
                .thenReturn(Mono.empty());
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .delete().uri("/accounts")
                .exchange()
                .expectStatus().isNoContent();
    }

    // Test negative case for deleteAccount when account is not found
    @Test
    public void deleteAccount_AccountNotFound_ShouldReturnNotFound() {
        when(accountService.deleteAccount(Mono.just(account.getId())))
                .thenReturn(Mono.error(new IllegalArgumentException("Account not found")));
        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .delete().uri("/accounts")
                //.contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Account not found");
    }*/
}
