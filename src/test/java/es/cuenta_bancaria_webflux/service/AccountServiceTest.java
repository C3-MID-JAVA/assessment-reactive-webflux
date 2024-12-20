package es.cuenta_bancaria_webflux.service;

import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.exception.CuentaNoEncontradaException;
import es.cuenta_bancaria_webflux.exception.SaldoInsuficienteException;
import es.cuenta_bancaria_webflux.mapper.AccountMapper;
import es.cuenta_bancaria_webflux.model.Account;
import es.cuenta_bancaria_webflux.model.Transaction;
import es.cuenta_bancaria_webflux.repository.AccountRepository;
import es.cuenta_bancaria_webflux.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountMapper dtoMapper;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Prueba para listar cuentas.")
    void testListarCuentas_Success() {
        List<Account> cuentas = List.of(
                new Account("1", "Carlos", BigDecimal.valueOf(1000), new ArrayList<>()),
                new Account("2", "Ana", BigDecimal.valueOf(2000), new ArrayList<>())
        );
        List<AccountDTO> cuentasDTO = List.of(
                new AccountDTO("1", "Carlos", BigDecimal.valueOf(1000), new ArrayList<>()),
                new AccountDTO("2", "Ana", BigDecimal.valueOf(2000), new ArrayList<>())
        );

        when(accountRepository.findAll()).thenReturn(Flux.fromIterable(cuentas));
        when(dtoMapper.toDto(cuentas.get(0))).thenReturn(cuentasDTO.get(0));
        when(dtoMapper.toDto(cuentas.get(1))).thenReturn(cuentasDTO.get(1));

        StepVerifier.create(accountService.listarCuentas())
                .expectNext(cuentasDTO.get(0))
                .expectNext(cuentasDTO.get(1))
                .verifyComplete();

        verify(accountRepository, times(1)).findAll();
        verify(dtoMapper, times(2)).toDto(any(Account.class));
    }


    @Test
    @DisplayName("Obtener una cuenta por ID.")
    void testObtenerCuentaPorId_Success() {
        Account cuenta = new Account("1", "Carlos", BigDecimal.valueOf(1000), null);
        AccountDTO cuentaDTO = new AccountDTO("1", "Carlos", BigDecimal.valueOf(1000), null);

        when(accountRepository.findById("1")).thenReturn(Mono.just(cuenta));
        when(dtoMapper.toDto(cuenta)).thenReturn(cuentaDTO);

        StepVerifier.create(accountService.obtenerCuentaPorId("1"))
                .expectNext(cuentaDTO)
                .verifyComplete();

        verify(accountRepository, times(1)).findById("1");
        verify(dtoMapper, times(1)).toDto(cuenta);
    }

    @Test
    @DisplayName("Obtener una cuenta por ID no guardado.")
    void testObtenerCuentaPorId_CuentaNoEncontrada() {
        when(accountRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(accountService.obtenerCuentaPorId("1"))
                .expectError(CuentaNoEncontradaException.class) // Espera que se lance la excepción
                .verify();

        verify(accountRepository, times(1)).findById("1");
        verify(dtoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Realizar una transaccion de forma correcta.")
    void testRealizarTransaccion_Success() {
        Account cuenta = new Account("1", "Carlos", BigDecimal.valueOf(1000), new ArrayList<>());
        AccountDTO cuentaDTO = new AccountDTO("1", "Carlos", BigDecimal.valueOf(900), new ArrayList<>());
        Transaction transaccion = new Transaction("1", BigDecimal.valueOf(100), "DEPOSITO_SUCURSAL", BigDecimal.ONE, "1");

        when(accountRepository.findById("1")).thenReturn(Mono.just(cuenta));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(cuenta));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaccion));
        when(dtoMapper.toDto(cuenta)).thenReturn(cuentaDTO);

        StepVerifier.create(accountService.realizarTransaccion("1", BigDecimal.valueOf(100), "DEPOSITO_SUCURSAL"))
                .expectNext(cuentaDTO)
                .verifyComplete();

        verify(accountRepository, times(1)).save(cuenta);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(dtoMapper, times(1)).toDto(cuenta);
    }

    @Test
    @DisplayName("Realizar una transaccion con saldo insuficiente.")
    void testRealizarTransaccion_SaldoInsuficiente() {
        Account cuenta = new Account("1", "Carlos", BigDecimal.valueOf(50), new ArrayList<>());

        when(accountRepository.findById("1")).thenReturn(Mono.just(cuenta));

        StepVerifier.create(accountService.realizarTransaccion("1", BigDecimal.valueOf(100), "RETIRO_CAJERO"))
                .expectError(SaldoInsuficienteException.class)
                .verify();

        verify(accountRepository, times(1)).findById("1");
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }


}
