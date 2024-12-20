package edisonrmedina.CityBank.controller;

import edisonrmedina.CityBank.dto.TransactionDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.entity.transaction.Transaction;
import edisonrmedina.CityBank.enums.TransactionType;
import edisonrmedina.CityBank.mapper.Mapper;
import edisonrmedina.CityBank.service.impl.BankAccountServiceImp;
import edisonrmedina.CityBank.service.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsControllerTest {

    private TransactionsServiceImpl transactionsService;
    @Autowired
    private WebTestClient webTestClient;


    @BeforeEach
    public void setUp() {
        transactionsService = Mockito.mock(TransactionsServiceImpl.class);
    }

    @Test
    void testGetAllTransactions() {
        // Preparar datos de prueba - Lista de Transaction
        List<Transaction> transactions = List.of(
                new Transaction(),
                new Transaction(),
                new Transaction(),
                new Transaction(),
                new Transaction(),
                new Transaction()
        );

        // Simular el comportamiento del servicio
        when(transactionsService.getAllTransactions()).thenReturn(Flux.fromIterable(transactions));

        // Llamar al método reactivo del servicio
        Flux<Transaction> result = transactionsService.getAllTransactions();

        // Verificar que las transacciones sean emitidas
        StepVerifier.create(result)
                .expectNextCount(transactions.size())  // Verifica que se emiten el número correcto de transacciones
                .expectComplete()                      // Verifica que el flujo se complete
                .verify();                             // Ejecuta la verificación

        // Verificar que el servicio fue llamado correctamente
        verify(transactionsService, times(1)).getAllTransactions();
    }

    @Test
    void testGetAllTransactionsNoData() {
        // Simular que el servicio no devuelve ninguna transacción (Flux vacío)
        when(transactionsService.getAllTransactions()).thenReturn(Flux.empty());

        // Llamar al método reactivo del servicio
        Flux<Transaction> result = transactionsService.getAllTransactions();

        // Verificar que no se emiten transacciones
        StepVerifier.create(result)
                .expectComplete()  // Verifica que el flujo se complete sin emitir ningún elemento
                .verify();         // Ejecuta la verificación

        // Verificar que el servicio fue llamado correctamente
        verify(transactionsService, times(1)).getAllTransactions();
    }

    @Test
    void testCreateTransaction() {
        // Preparar datos de prueba - TransactionDTO
        TransactionDTO transactionRequest = new TransactionDTO();
        transactionRequest.setTransactionCost(BigDecimal.TEN);
        transactionRequest.setTransactionType(TransactionType.WITHDRAW_ATM);
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setBankAccountId("676044f6c0345b0069778536");

        // Crear la transacción esperada
        TransactionDTO createdTransaction = new TransactionDTO();
        createdTransaction.setTransactionCost(transactionRequest.getTransactionCost());
        createdTransaction.setTransactionType(transactionRequest.getTransactionType());
        createdTransaction.setAmount(transactionRequest.getAmount());
        createdTransaction.setBankAccountId(transactionRequest.getBankAccountId());

        // Convertir el objeto de la transacción creada en un Mono
        Mono<TransactionDTO> monoCreatedTransaction = Mono.just(createdTransaction);

        // Convertir el DTO en una entidad Transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionCost(transactionRequest.getTransactionCost());
        transaction.setType(transactionRequest.getTransactionType());
        transaction.setAmount(transactionRequest.getAmount());

        Mono<Transaction> monoTransaction = transactionsService.createTransaction(transaction);

        // Configura el mock para devolver la transacción creada
        when(transactionsService.createTransaction(any(Transaction.class)))
                .thenReturn(monoTransaction);

        // Realiza la solicitud al endpoint usando WebTestClient
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions/transactions/{accountId}") // Ruta con la variable accountId
                        .build("676044f6c0345b0069778536")) // Valor del accountId
                .bodyValue(transactionRequest) // Cuerpo de la solicitud con los datos de la transacción
                .exchange() // Ejecuta la solicitud
                .expectStatus().isCreated() // Verifica que el estado HTTP sea 201 CREATED
                .expectBody() // Verifica el cuerpo de la respuesta
                .jsonPath("$.transactionCost").isEqualTo(1) // Verifica el costo de la transacción
                .jsonPath("$.type").isEqualTo("WITHDRAW_ATM") // Verifica el tipo de transacción
                .jsonPath("$.amount").isEqualTo(100);
    }

    @Test
    void testCreateTransaction_invalidTransaction() {

            // Preparar datos de prueba - TransactionDTO
            TransactionDTO transactionRequest = new TransactionDTO();
            transactionRequest.setTransactionCost(BigDecimal.TEN);
            transactionRequest.setTransactionType(TransactionType.DEPOSIT_OUT);
            transactionRequest.setAmount(BigDecimal.valueOf(100.0));
            transactionRequest.setBankAccountId("676044f6c0345b0069778536");

            // Crear la transacción esperada
            TransactionDTO createdTransaction = new TransactionDTO();
            createdTransaction.setTransactionCost(transactionRequest.getTransactionCost());
            createdTransaction.setTransactionType(transactionRequest.getTransactionType());
            createdTransaction.setAmount(transactionRequest.getAmount());
            createdTransaction.setBankAccountId(transactionRequest.getBankAccountId());

            // Convertir el objeto de la transacción creada en un Mono
            Mono<TransactionDTO> monoCreatedTransaction = Mono.just(createdTransaction);

            // Convertir el DTO en una entidad Transaction
            Transaction transaction = new Transaction();
            transaction.setTransactionCost(transactionRequest.getTransactionCost());
            transaction.setType(transactionRequest.getTransactionType());
            transaction.setAmount(transactionRequest.getAmount());

            Mono<Transaction> monoTransaction = transactionsService.createTransaction(transaction);

            // Configura el mock para devolver la transacción creada
            when(transactionsService.createTransaction(any(Transaction.class)))
                    .thenReturn(monoTransaction);

            // Realiza la solicitud al endpoint usando WebTestClient
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions/transactions/{accountId}")
                        .build("676044f6c0345b0069778536"))
                .bodyValue(transactionRequest)
                .exchange()
                .expectStatus().is5xxServerError();

    }

}
