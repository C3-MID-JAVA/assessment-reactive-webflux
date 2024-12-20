package ec.com.example.bank_account.controller;

import ec.com.example.bank_account.dto.TypeAccountRequestDTO;
import ec.com.example.bank_account.dto.TypeAccountResponseDTO;
import ec.com.example.bank_account.service.TypeAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TypeAccountControllerTest {

    @Mock
    private TypeAccountService typeAccountService;

    @InjectMocks
    private TypeAccountController typeAccountController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(typeAccountController).build();
    }

    @Test
    void createTypeAccount_ShouldReturnCreatedStatus_WhenAccountTypeIsCreatedSuccessfully() throws Exception {
        TypeAccountRequestDTO typeAccountRequestDTO = new TypeAccountRequestDTO("Debit account",
                "User debit account.", "ACTIVE");
        TypeAccountResponseDTO typeAccountResponseDTO = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");
        when(typeAccountService.createTypeAccount(any(TypeAccountRequestDTO.class))).thenReturn(Mono.just(typeAccountResponseDTO));

        webTestClient.post()
                .uri("/api/types-account")
                .bodyValue(typeAccountRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.type").isEqualTo("Debit account");

        verify(typeAccountService).createTypeAccount(any(TypeAccountRequestDTO.class));
    }

    @Test
    void getTypesAccount_ShouldReturnListOfAccountTypes_WhenAccountsExist() throws Exception {
        TypeAccountResponseDTO typesAccount = new TypeAccountResponseDTO("Debit account",
                "User debit account.", "ACTIVE");
        when(typeAccountService.getAllTypeAccount()).thenReturn(Flux.just(typesAccount));

        webTestClient.get()
                .uri("/api/types-account")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TypeAccountResponseDTO.class)
                .hasSize(1);

        verify(typeAccountService).getAllTypeAccount();
    }

}
