package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.dto.TransactionRequestDTO;
import ec.com.example.bank_account.dto.TransactionResponseDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.service.TransactionService;
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
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;
    
    @InjectMocks
    private TransactionController transactionController;

    private AccountResponseDTO accountResponseDTO;
    private TypeTransactionResponseDTO typeTransactionResponseDTO;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(transactionController).build();

        UserResponseDTO userResponse = new UserResponseDTO("Diego", "Loor",
                "1310000000", "diego.loor@sofka.com.co", "ACTIVE");
        TypeAccountResponseDTO typeAccountResponse = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");
        accountResponseDTO = new AccountResponseDTO("2200000000", new BigDecimal(100),
                new BigDecimal(0), "ACTIVE", Mono.just(userResponse), Mono.just(typeAccountResponse));

        typeTransactionResponseDTO = new TypeTransactionResponseDTO("Deposit from branch",
                "Deposits made from a branch.", new BigDecimal(100), true, false, "ACTIVE");
    }

    @Test
    void createTransaction_ShouldReturnCreatedStatus_WhenTransactionIsCreatedSuccessfully() {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(new BigDecimal(100), new Date(),
                "2200000000", "transaction made in Manabí.", "ACTIVE", "test");
        TransactionResponseDTO transactionResponse = new TransactionResponseDTO("2200000000", "1", new BigDecimal(100), new Date(),
                "ACTIVE", Mono.just(accountResponseDTO), Mono.just(typeTransactionResponseDTO));
        when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenReturn(Mono.just(transactionResponse));

        webTestClient.post()
                .uri("/api/transactions")
                .bodyValue(transactionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.value").isEqualTo(new BigDecimal(100));

        verify(transactionService).createTransaction(any(TransactionRequestDTO.class));
    }

    @Test
    void getAllTransactions_ShouldReturnOkStatus_WhenTransactionsAreRetrievedSuccessfully() {
        TransactionResponseDTO transactions = new TransactionResponseDTO("2200000000", "1", new BigDecimal(100), new Date(),
                "ACTIVE", Mono.just(accountResponseDTO), Mono.just(typeTransactionResponseDTO));
        when(transactionService.getAllTransactions()).thenReturn(Flux.just(transactions));

        webTestClient.get()
                .uri("/api/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountResponseDTO.class)
                .hasSize(1);

        verify(transactionService).getAllTransactions();
    }
}