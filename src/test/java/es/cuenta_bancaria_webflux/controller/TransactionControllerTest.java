package es.cuenta_bancaria_webflux.controller;
import es.cuenta_bancaria_webflux.controllers.TransactionController;
import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import es.cuenta_bancaria_webflux.service.ITransactionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    @Mock
    private ITransactionService transaccionServicio;

    @InjectMocks
    private TransactionController transactionController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(transactionController).build();
    }

    @Test
    @DisplayName("Listar transacciones de forma correcta.")
    void testListTransaction_Success() {
        TransactionDTO t1 = new TransactionDTO("1", BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");
        TransactionDTO t2 = new TransactionDTO("2", BigDecimal.valueOf(200), "RETIRO", BigDecimal.ONE, "123");
        List<TransactionDTO> transacciones = Arrays.asList(t1, t2);

        when(transaccionServicio.listarTransacciones()).thenReturn(Flux.fromIterable(transacciones));

        webTestClient.get().uri("/api/transacciones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionDTO.class).hasSize(2)
                .contains(t1, t2);
    }

    @Test
    @DisplayName("Lista de transacciones vacias.")
    void testListTransaction_NoContent() {
        when(transaccionServicio.listarTransacciones()).thenReturn(Flux.empty());

        webTestClient.get().uri("/api/transacciones")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Transaccion con ID encontrado.")
    void testGetTransactionById_Found() {
        String transactionId = "1";
        TransactionDTO t1 = new TransactionDTO(transactionId, BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");

        when(transaccionServicio.obtenerTransaccionPorId(transactionId)).thenReturn(Mono.just(t1));
        webTestClient.get().uri("/api/transacciones/{id}", transactionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionDTO.class)
                .isEqualTo(t1);
    }

    @Test
    @DisplayName("Transaccion con ID no encontrado.")
    void testGetTransactionById_NotFound() {
        String transactionId = "999";

        when(transaccionServicio.obtenerTransaccionPorId(transactionId)).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/transacciones/{id}", transactionId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Transaccion con ID invalido.")
    void testGetTransactionById_InvalidId() {
        String invalidId = "   ";

        webTestClient.get().uri("/api/transacciones/{id}", invalidId)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
