package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private UserResponseDTO userResponse;
    private TypeAccountResponseDTO typeAccountResponse;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(accountController).build();
        userResponse = new UserResponseDTO("Diego", "Loor", "1310000000", "diego.loor@sofka.com.co", "ACTIVE");
        typeAccountResponse = new TypeAccountResponseDTO("Debit account", "User debit account.", "ACTIVE");
    }

    @Test
    void createAccount_ShouldReturnCreatedStatus_WhenAccountIsCreatedSuccessfully() {
        AccountRequestDTO accountRequest = new AccountRequestDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", "test", "test");
        AccountResponseDTO accountResponse = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponse));
        when(accountService.createAccount(any(AccountRequestDTO.class))).thenReturn(Mono.just(accountResponse));

        webTestClient.post()
                .uri("/api/accounts")
                .bodyValue(accountRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.number").isEqualTo("2200000000");

        verify(accountService).createAccount(any(AccountRequestDTO.class));
    }

    @Test
    void getAccounts_ShouldReturnOkStatus_WhenAccountsAreRetrievedSuccessfully() {
        AccountResponseDTO accountResponse = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponse));
        when(accountService.getAllAccounts()).thenReturn(Flux.just(accountResponse));

        webTestClient.get()
                .uri("/api/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TypeAccountResponseDTO.class)
                .hasSize(1);

        verify(accountService).getAllAccounts();
    }

}