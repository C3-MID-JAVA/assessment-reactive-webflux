package edisonrmedina.CityBank.service;

import edisonrmedina.CityBank.dto.BankAccountDTO;
import edisonrmedina.CityBank.dto.CreateBankAccountDTO;
import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.repository.BankAccountRepository;
import edisonrmedina.CityBank.service.impl.BankAccountServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImpTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImp bankAccountServiceImp;

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount("John Doe", BigDecimal.valueOf(1000));
        bankAccount.setId("123");
    }

    @Test
    void testGetBankAccount_PositiveCase_Reactive() {
        // Arrange: Crear el objeto BankAccount esperado
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder("John Doe");
        bankAccount.setBalance(BigDecimal.valueOf(1000));
        bankAccount.setId("123");

        // Simular que el repositorio devuelve un Mono con el BankAccount cuando se le pasa el ID "123"
        Mockito.when(bankAccountRepository.findById("123")).thenReturn(Mono.just(bankAccount));

        // Act: Llamar al servicio para obtener la cuenta bancaria
        Mono<BankAccount> result = bankAccountServiceImp.getBankAccount("123");

        // Assert: Verificar el resultado usando StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(account ->
                        account.getAccountHolder().equals("John Doe") &&
                                account.getBalance().compareTo(BigDecimal.valueOf(1000)) == 0)
                .verifyComplete();
    }

    @Test
    void testGetBankAccount_NegativeCase_Reactive() {
        // Arrange: Simular que el repositorio devuelve un Mono vacío
        Mockito.when(bankAccountRepository.findById("999")).thenReturn(Mono.empty());

        // Act: Llamar al servicio para obtener la cuenta bancaria
        Mono<BankAccount> result = bankAccountServiceImp.getBankAccount("999");

        // Assert: Verificar que el resultado sea vacío
        StepVerifier.create(result)
                .expectNextCount(0)  // No se emite ningún elemento
                .verifyComplete();   // El flujo se completa correctamente
    }

    @Test
    void testRegister_PositiveCase_Reactive() {
        // Arrange: Crear un objeto CreateBankAccountDTO y el esperado BankAccountDTO
        CreateBankAccountDTO createBankAccountDTO = new CreateBankAccountDTO();
        createBankAccountDTO.setAccountHolder("John Doe");
        createBankAccountDTO.setBalance(BigDecimal.valueOf(1000));

        BankAccountDTO expectedBankAccountDTO = new BankAccountDTO();
        expectedBankAccountDTO.setAccountHolder("John Doe");
        expectedBankAccountDTO.setBalance(BigDecimal.valueOf(1000));

        // Simular los métodos internos del servicio
        Mockito.when(bankAccountRepository.save(Mockito.any(BankAccount.class)))
                .thenAnswer(invocation -> {
                    BankAccount savedAccount = invocation.getArgument(0);
                    savedAccount.setId("123");
                    return Mono.just(savedAccount);
                });

        // Act: Llamar al método register
        Mono<BankAccountDTO> result = bankAccountServiceImp.register(Mono.just(createBankAccountDTO));

        // Assert: Verificar los datos emitidos usando StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getAccountHolder().equals("John Doe") &&
                                dto.getBalance().compareTo(BigDecimal.valueOf(1000)) == 0)
                .verifyComplete();
    }

    @Test
    void testRegister_NegativeCase_MissingAnyAttribute_Reactive() {
        // Arrange: Crear un CreateBankAccountDTO inválido
        CreateBankAccountDTO invalidCreateBankAccountDTO = new CreateBankAccountDTO();
        invalidCreateBankAccountDTO.setBalance(BigDecimal.valueOf(1000)); // Falta el titular

        // Act & Assert: Verificar que se lanza la excepción genérica con el mensaje esperado
        StepVerifier.create(bankAccountServiceImp.register(Mono.just(invalidCreateBankAccountDTO)))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Error inesperado al registrar la cuenta bancaria"))
                .verify();
    }


    @Test
    void testUpdateBankAccount_PositiveCase_Reactive() {
        // Arrange: Crear el objeto BankAccount a actualizar
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder("John Doe");
        bankAccount.setBalance(BigDecimal.valueOf(1000));
        bankAccount.setId("123");

        // Simular que el repositorio guarda el objeto y devuelve un Mono<Void>
        Mockito.when(bankAccountRepository.save(bankAccount)).thenReturn(Mono.just(bankAccount));

        // Act: Llamar al servicio para actualizar la cuenta bancaria
        Mono<Void> result = bankAccountServiceImp.updateBankAccount(bankAccount);

        // Assert: Verificar que el repositorio fue invocado y el flujo se completa exitosamente
        StepVerifier.create(result)
                .verifyComplete();

        // Verificar que el repositorio fue invocado una vez con el objeto correcto
        Mockito.verify(bankAccountRepository, Mockito.times(1)).save(bankAccount);
    }


}
