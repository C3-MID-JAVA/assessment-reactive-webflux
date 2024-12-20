package es.cuenta_bancaria_webflux.controller;

import es.cuenta_bancaria_webflux.controllers.AccountController;
import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.model.Transaction;
import es.cuenta_bancaria_webflux.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

    @Mock
    private IAccountService cuentaServicio;

    @InjectMocks
    private AccountController accountController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(accountController).build();
    }

    @Test
    @DisplayName("Listar cuentas.")
    void testListAccount_Success() {
        AccountDTO a1 = new AccountDTO("1", "Juan Perez", BigDecimal.valueOf(1000), new ArrayList<>());
        AccountDTO a2 = new AccountDTO("2", "Maria Lopez", BigDecimal.valueOf(2000), new ArrayList<>());
        List<AccountDTO> accounts = Arrays.asList(a1, a2);

        when(cuentaServicio.listarCuentas()).thenReturn(Flux.fromIterable(accounts));

        webTestClient.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountDTO.class).hasSize(2)
                .contains(a1, a2);
    }

    @Test
    @DisplayName("Listar cuentas vacias.")
    void testListAccount_NoContent() {
        when(cuentaServicio.listarCuentas()).thenReturn(Flux.empty());

        webTestClient.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isNoContent(); // Espera un 204 NO_CONTENT
    }

    @Test
    @DisplayName("Buscar cuenta con ID.")
    void testSearchAccountById_Found() {
        String accountId = "1";
        AccountDTO a1 = new AccountDTO(accountId, "Juan Perez", BigDecimal.valueOf(1000), null);

        when(cuentaServicio.obtenerCuentaPorId(accountId)).thenReturn(Mono.just(a1));

        webTestClient.get().uri("/api/cuentas/{id}", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDTO.class)
                .isEqualTo(a1);
    }

    @Test
    @DisplayName("Buscar cuenta con ID incorrecto.")
    void testSearchAccountById_NotFound() {
        String accountId = "999";

        when(cuentaServicio.obtenerCuentaPorId(accountId)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/cuentas/{id}", accountId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Creacion de cuenta de forma correcta.")
    void testCreateAccount_Success() {
        AccountDTO request = new AccountDTO("1", "Juan Perez", BigDecimal.valueOf(1000), new ArrayList<>());
        AccountDTO response = new AccountDTO("1", "Juan Perez", BigDecimal.valueOf(1000), new ArrayList<>());

        when(cuentaServicio.crearCuenta(request)).thenReturn(Mono.just(response));

        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountDTO.class)
                .isEqualTo(response);
    }

    @Test
    @DisplayName("Creacion de cuenta fallida.")
    void testCreateAccount_Failure() {
        AccountDTO request = new AccountDTO(null, "Juan Perez", BigDecimal.valueOf(1000), null);

        when(cuentaServicio.crearCuenta(request)).thenReturn(Mono.empty());

        webTestClient.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    @DisplayName("Realizar una transaccion de forma correcta.")
    void testMakeTransaction_Success() {
        String accountId = "1";
        BigDecimal monto = BigDecimal.valueOf(500);
        String tipo = "DEPOSITO";

        AccountDTO updatedAccount = new AccountDTO(accountId, "Juan Perez", BigDecimal.valueOf(1500), new ArrayList<>());
        Transaction transaction = new Transaction(null, monto,tipo,  BigDecimal.ONE, "1");
        // Mock del servicio
        when(cuentaServicio.realizarTransaccion(eq(accountId), eq(monto), eq(tipo)))
                .thenReturn(Mono.just(updatedAccount));

        // Prueba del controlador
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/cuentas/{id}/transacciones").build(accountId))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDTO.class)
                .isEqualTo(updatedAccount);
    }
}