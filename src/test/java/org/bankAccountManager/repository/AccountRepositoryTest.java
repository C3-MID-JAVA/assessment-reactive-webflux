package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Account;
import org.bankAccountManager.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class AccountRepositoryTest {

    /*@Autowired
    private AccountRepository accountRepository;

    private Account account1;
    private Account account2;

    @BeforeEach
    public void setUp() {
        // Limpia la base de datos
        accountRepository.deleteAll();

        // Crear cuentas de prueba
        account1 = new Account();
        account1.setId(1L);
        account1.setAccountNumber("123456789");
        account1.setAccountType("savings");
        account1.setBalance(500);

        Transaction transaction1 = new Transaction();
        transaction1.setId("txn1");
        transaction1.setAmount(100);
        transaction1.setType("deposit");

        account1.setTransactions(List.of(transaction1));

        account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("987654321");
        account2.setAccountType("checking");
        account2.setBalance(1000);

        accountRepository.saveAll(Arrays.asList(account1, account2));
    }

    @Test
    public void testFindAccountById_Positive() {
        // Prueba positiva: Encontrar una cuenta existente
        Account foundAccount = accountRepository.findAccountById(1);
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getAccountNumber()).isEqualTo("123456789");
    }

    @Test
    public void testFindAccountById_Negative() {
        // Prueba negativa: Cuenta inexistente
        Account foundAccount = accountRepository.findAccountById(999);
        assertThat(foundAccount).isNull();
    }

    @Test
    public void testExistsById_Positive() {
        // Prueba positiva: Verificar si existe una cuenta con ID 1
        Boolean exists = accountRepository.existsById(1);
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsById_Negative() {
        // Prueba negativa: Verificar una cuenta inexistente
        Boolean exists = accountRepository.existsById(999);
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindAll_Positive() {
        // Prueba positiva: Verificar que se obtengan todas las cuentas
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    public void testFindAll_Negative() {
        // Prueba negativa: Vaciar la base de datos y verificar
        accountRepository.deleteAll();
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isEmpty();
    }

    @Test
    public void testGetAccountByTransactions_Positive() {
        // Prueba positiva: Encontrar una cuenta por sus transacciones
        Transaction transaction = account1.getTransactions().get(0);
        Account foundAccount = accountRepository.getAccountByTransactions(List.of(transaction));
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getAccountNumber()).isEqualTo("123456789");
    }

    @Test
    public void testGetAccountByTransactions_Negative() {
        // Prueba negativa: Intentar encontrar una cuenta con una transacción inexistente
        Transaction nonExistentTransaction = new Transaction();
        nonExistentTransaction.setId("nonexistent");
        Account foundAccount = accountRepository.getAccountByTransactions(List.of(nonExistentTransaction));
        assertThat(foundAccount).isNull();
    }

    @Test
    public void testSaveAccount_Positive() {
        // Prueba positiva: Guardar una nueva cuenta
        Account newAccount = new Account();
        newAccount.setId(3L);
        newAccount.setAccountNumber("111222333");
        newAccount.setAccountType("savings");
        newAccount.setBalance(300);
        Account savedAccount = accountRepository.save(newAccount);
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getId()).isEqualTo(3L);
    }

    @Test
    public void testSaveAccount_Negative() {
        // Prueba negativa: Intentar guardar una cuenta con un ID nulo
        Account invalidAccount = new Account();
        invalidAccount.setAccountNumber("444555666");
        invalidAccount.setAccountType("checking");
        assertThrows(Exception.class, () -> accountRepository.save(invalidAccount));
    }*/
}
