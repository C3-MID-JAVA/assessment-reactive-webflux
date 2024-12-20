package es.cuenta_bancaria_webflux.repository;
import es.cuenta_bancaria_webflux.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setTitular("Juan Perez");
        account.setSaldo(BigDecimal.valueOf(1000.0));
        account.setTransacciones(Collections.emptyList());
    }

    @Test
    @DisplayName("Guardar una cuenta en el repositorio")
    public void testSaveAccount() {
        Mono<Account> saveMono = accountRepository.save(account);

        StepVerifier.create(saveMono)
                .assertNext(savedAccount -> {
                    assertNotNull(savedAccount.getIdCuenta());
                    assertEquals(account.getTitular(), savedAccount.getTitular());
                    assertEquals(account.getSaldo(), savedAccount.getSaldo());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Encontrar una cuenta por ID.")
    public void testFindAccountById() {
        Mono<Account> findMono = accountRepository.save(account)
                .flatMap(savedAccount -> accountRepository.findById(savedAccount.getIdCuenta()));

        StepVerifier.create(findMono)
                .assertNext(foundAccount -> {
                    assertNotNull(foundAccount);
                    assertEquals(account.getTitular(), foundAccount.getTitular());
                    assertEquals(account.getSaldo(), foundAccount.getSaldo());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Eliminar una cuenta del repositorio")
    public void testDeleteAccount() {
        Mono<Void> deleteMono = accountRepository.save(account)
                .flatMap(savedAccount -> accountRepository.delete(savedAccount)
                        .then(accountRepository.findById(savedAccount.getIdCuenta()).then()));

        StepVerifier.create(deleteMono)
                .expectNextCount(0) // No debería encontrar nada
                .verifyComplete();
    }
}
