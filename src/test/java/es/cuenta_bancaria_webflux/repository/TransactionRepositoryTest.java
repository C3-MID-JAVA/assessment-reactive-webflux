package es.cuenta_bancaria_webflux.repository;

import es.cuenta_bancaria_webflux.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction;

    @BeforeEach
    public void setUp(){
        transaction = new Transaction();
        transaction.setMonto(BigDecimal.valueOf(1000.0));
        transaction.setTipo("Depósito");
        transaction.setCosto(BigDecimal.valueOf(5.0));
        transaction.setIdCuenta("some-id-new");

    }
    @Test
    @DisplayName("Guardar una transacción en el repositorio")
    public void testSaveTransaction() {
        Mono<Transaction> saveMono = transactionRepository.save(transaction);

        StepVerifier.create(saveMono)
                .assertNext(savedTransaction -> {
                    assertNotNull(savedTransaction.getIdTransaccion());
                    assertEquals(transaction.getMonto(), savedTransaction.getMonto());
                    assertEquals(transaction.getTipo(), savedTransaction.getTipo());
                    assertEquals(transaction.getCosto(), savedTransaction.getCosto());
                    assertEquals(transaction.getIdCuenta(), savedTransaction.getIdCuenta());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Buscar una transacción por ID")
    public void testFindTransactionById() {
        Mono<Transaction> findMono = transactionRepository.save(transaction)
                .flatMap(savedTransaction -> transactionRepository.findById(savedTransaction.getIdTransaccion()));

        StepVerifier.create(findMono)
                .assertNext(foundTransaction -> {
                    assertNotNull(foundTransaction);
                    assertEquals(transaction.getMonto(), foundTransaction.getMonto());
                    assertEquals(transaction.getTipo(), foundTransaction.getTipo());
                    assertEquals(transaction.getCosto(), foundTransaction.getCosto());
                    assertEquals(transaction.getIdCuenta(), foundTransaction.getIdCuenta());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Eliminar una transacción del repositorio")
    public void testDeleteTransaction() {
        Mono<Void> deleteMono = transactionRepository.save(transaction)
                .flatMap(savedTransaction -> transactionRepository.delete(savedTransaction)
                        .then(transactionRepository.findById(savedTransaction.getIdTransaccion())).then());

        StepVerifier.create(deleteMono)
                .expectNextCount(0) // No debería encontrar nada
                .verifyComplete();
    }
}
