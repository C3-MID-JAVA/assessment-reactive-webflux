package es.cuenta_bancaria_webflux.service;

import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import es.cuenta_bancaria_webflux.mapper.TransactionMapper;
import es.cuenta_bancaria_webflux.model.Transaction;
import es.cuenta_bancaria_webflux.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper dtoMapper;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Listar Transacciones")
    void testListTransactions() {
        Transaction t1 = new Transaction("1", BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");
        Transaction t2 = new Transaction("2", BigDecimal.valueOf(200), "RETIRO", BigDecimal.ONE, "123");

        TransactionDTO t1Dto = new TransactionDTO("1", BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");
        TransactionDTO t2Dto = new TransactionDTO("2", BigDecimal.valueOf(200), "RETIRO", BigDecimal.ONE, "123");

        when(transactionRepository.findAll()).thenReturn(Flux.just(t1, t2));
        when(dtoMapper.toDto(t1)).thenReturn(t1Dto);
        when(dtoMapper.toDto(t2)).thenReturn(t2Dto);

        StepVerifier.create(transactionService.listarTransacciones())
                .expectNext(t1Dto)
                .expectNext(t2Dto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Obtener una transaccion por ID.")
    void testGetTransactionById_Found() {
        String transactionId = "1";
        Transaction t1 = new Transaction(transactionId, BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");
        TransactionDTO t1Dto = new TransactionDTO(transactionId, BigDecimal.valueOf(100), "DEPOSITO", BigDecimal.ZERO, "123");

        when(transactionRepository.findById(transactionId)).thenReturn(Mono.just(t1));
        when(dtoMapper.toDto(t1)).thenReturn(t1Dto);

        StepVerifier.create(transactionService.obtenerTransaccionPorId(transactionId))
                .expectNext(t1Dto)
                .verifyComplete();
    }

    @Test
    @DisplayName("Obtener una transaccion con ID no guardado.")
    void testGetTransactionById_NotFound() {
        String transactionId = "999";

        when(transactionRepository.findById(transactionId)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.obtenerTransaccionPorId(transactionId))
                .expectError(RuntimeException.class)  // Espera que se lance RuntimeException
                .verify();
    }
}
