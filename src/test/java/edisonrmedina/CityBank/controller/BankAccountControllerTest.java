package edisonrmedina.CityBank.controller;

import edisonrmedina.CityBank.dto.BankAccountDTO;
import edisonrmedina.CityBank.dto.CreateBankAccountDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.service.BankAccountService;
import edisonrmedina.CityBank.service.impl.BankAccountServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private BankAccountServiceImp bankAccountServiceImpMock;

    @BeforeEach
    public void setUp() {
        bankAccountServiceImpMock = Mockito.mock(BankAccountServiceImp.class);
    }

    @Test
    public void testGetBankAccount() {
        //Arrange
        String accountId = "675e962784c89c424d3bc7b6";  // ID de ejemplo
        Mono<BankAccount> bankAccount = bankAccountServiceImpMock.getBankAccount(accountId);

        // Configura el mock para el servicio
        when(bankAccountServiceImpMock.getBankAccount(accountId)).thenReturn(bankAccount);

        // Realiza la solicitud al endpoint usando WebTestClient
        webTestClient.get()
                .uri("/bank-account/{id}", accountId)  // URL dinámica con path variable
                .exchange()  // Realiza la solicitud
                .expectStatus().isOk()  // Verifica que el estado sea 200 OK
                .expectBody()  // Verifica el cuerpo de la respuesta
                .jsonPath("$.id").isEqualTo(accountId)  // Verifica que el id es correcto
                .jsonPath("$.accountHolder").isEqualTo("Edison")
                .jsonPath("$.balance").isEqualTo("395.5");  // Verifica que el balance es correcto
    }

    @Test
    public void testCreateBankAccount() {
        // Crea el DTO para la creación de la cuenta
        CreateBankAccountDTO newAccountDTO = new CreateBankAccountDTO("Michael", BigDecimal.valueOf(231));

        // Convierte el objeto en un Mono
        Mono<CreateBankAccountDTO> monoNewAccountDTO = Mono.just(newAccountDTO);

        // Crea el DTO para la cuenta creada
        BankAccountDTO createdAccount = new BankAccountDTO("675e962784c89c424d3bc7b6", "Michael", BigDecimal.valueOf(231));

        // Convierte el objeto en un Mono
        Mono<BankAccountDTO> monoCreatedAccount = Mono.just(createdAccount);

        // Configura el mock para devolver la cuenta creada
        when(bankAccountServiceImpMock.register(any(Mono.class)))
                .thenReturn(monoCreatedAccount);

        // Realiza la solicitud al endpoint usando WebTestClient
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/bank-account")
                        .queryParam("accountHolder", "Michael")
                        .queryParam("balance", 231)
                        .build())
                .exchange() // Realiza la solicitud
                .expectStatus().isEqualTo(HttpStatus.CREATED) // Verifica el estado HTTP 201
                .expectBody() // Verifica el cuerpo de la respuesta
                .jsonPath("$.accountHolder").isEqualTo("Michael") // Verifica que el ID es correcto
                .jsonPath("$.balance").isEqualTo(231); // Verifica que el balance es correcto
    }

}
