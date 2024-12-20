package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.TypeTransactionRequestDTO;
import ec.com.example.bank_account.dto.TypeTransactionResponseDTO;
import ec.com.example.bank_account.service.TypeTransactionService;
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
public class TypeTransactionControllerTest {

    @Mock
    private TypeTransactionService typeTransactionService;

    @InjectMocks
    private TypeTransactionController typeTransactionController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(typeTransactionController).build();
    }

    @Test
    void createTypeTransaction_ShouldReturnCreatedStatus_WhenTransactionTypeIsCreatedSuccessfully() {
        TypeTransactionRequestDTO typeTransactionRequestDTO = new TypeTransactionRequestDTO("Deposit from branch",
                "Deposits made from a branch.", new BigDecimal(100), true, false, "ACTIVE");
        TypeTransactionResponseDTO typeTransactionResponseDTO = new TypeTransactionResponseDTO("Deposit from branch",
                "Deposits made from a branch.", new BigDecimal(100), true, false, "ACTIVE");
        when(typeTransactionService.createTypeTransaction(any(TypeTransactionRequestDTO.class))).thenReturn(Mono.just(typeTransactionResponseDTO));

        webTestClient.post()
                .uri("/api/types-transaction")
                .bodyValue(typeTransactionRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.type").isEqualTo("Deposit from branch");

        verify(typeTransactionService).createTypeTransaction(any(TypeTransactionRequestDTO.class));
    }

    @Test
    void getAllTypeTransactions_ShouldReturnList_WhenTransactionsExist() {
        TypeTransactionResponseDTO transactions = new TypeTransactionResponseDTO("Deposit from branch",
                "Deposits made from a branch.", new BigDecimal(100), true, false, "ACTIVE");
        when(typeTransactionService.getAllTypeTransactions()).thenReturn(Flux.just(transactions));

        webTestClient.get()
                .uri("/api/types-transaction")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TypeTransactionResponseDTO.class)
                .hasSize(1);

        verify(typeTransactionService).getAllTypeTransactions();
    }
}