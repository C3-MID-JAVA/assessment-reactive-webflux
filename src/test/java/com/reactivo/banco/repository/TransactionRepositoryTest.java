package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Transaction;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import com.reactivo.banco.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testSaveAndFindById() {
        Transaction transaction = new Transaction();
        transaction.setAccountId("12345");
        transaction.setAmount(new BigDecimal("100.0"));
        transaction.setDescription("Pago de factura");
        transaction.setTransactionType("crédito");
        transaction.setDate(LocalDate.now());

        Mono<Transaction> saveMono = transactionRepository.save(transaction);

        StepVerifier.create(saveMono)
                .assertNext(savedTransaction -> {
                    assertThat(savedTransaction.getId()).isNotNull();
                    assertThat(savedTransaction.getAccountId()).isEqualTo("12345");
                    assertThat(savedTransaction.getAmount()).isEqualTo(new BigDecimal("100.0"));
                    assertThat(savedTransaction.getDescription()).isEqualTo("Pago de factura");
                    assertThat(savedTransaction.getTransactionType()).isEqualTo("crédito");
                    assertThat(savedTransaction.getDate()).isEqualTo(LocalDate.now());
                })
                .verifyComplete();

        Mono<Transaction> findMono = transactionRepository.findById(transaction.getId());

        StepVerifier.create(findMono)
                .assertNext(foundTransaction -> {
                    assertThat(foundTransaction.getId()).isEqualTo(transaction.getId());
                    assertThat(foundTransaction.getAccountId()).isEqualTo("12345");
                    assertThat(foundTransaction.getAmount()).isEqualTo(new BigDecimal("100.0"));
                    assertThat(foundTransaction.getDescription()).isEqualTo("Pago de factura");
                    assertThat(foundTransaction.getTransactionType()).isEqualTo("crédito");
                    assertThat(foundTransaction.getDate()).isEqualTo(LocalDate.now());
                })
                .verifyComplete();
    }

//    @Test
//    public void testFindByAccountId() {
//        Transaction transaction1 = new Transaction();
//        transaction1.setAccountId("12345");
//        transaction1.setAmount(new BigDecimal("100.0"));
//        transaction1.setDescription("Pago de factura");
//        transaction1.setTransactionType("crédito");
//        transaction1.setDate(LocalDate.now());
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setAccountId("12345");
//        transaction2.setAmount(new BigDecimal("50.0"));
//        transaction2.setDescription("Pago de comida");
//        transaction2.setTransactionType("débito");
//        transaction2.setDate(LocalDate.now());
//
//        transactionRepository.save(transaction1).block();
//        transactionRepository.save(transaction2).block();
//
//        Mono<Transaction> findMono1 = transactionRepository.findByAccountId("12345");
//
//        StepVerifier.create(findMono1)
//                .assertNext(foundTransaction -> {
//                    assertThat(foundTransaction.getAccountId()).isEqualTo("12345");
//                    assertThat(foundTransaction.getAmount()).isEqualTo(new BigDecimal("100.0"));
//                    assertThat(foundTransaction.getDescription()).isEqualTo("Pago de factura");
//                })
//                .verifyComplete();
//    }

    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId("12345");
        transaction.setAmount(new BigDecimal("100.0"));
        transaction.setDescription("Pago de factura");
        transaction.setTransactionType("crédito");
        transaction.setDate(LocalDate.now());

        Mono<Transaction> saveMono = transactionRepository.save(transaction);
        Transaction savedTransaction = saveMono.block();

        Mono<Void> deleteMono = transactionRepository.deleteById(savedTransaction.getId());

        StepVerifier.create(deleteMono)
                .verifyComplete();

        Mono<Transaction> findMono = transactionRepository.findById(savedTransaction.getId());

        StepVerifier.create(findMono)
                .expectNextCount(0)
                .verifyComplete();
    }
}
