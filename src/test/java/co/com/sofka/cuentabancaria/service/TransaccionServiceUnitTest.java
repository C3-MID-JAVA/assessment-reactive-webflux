package co.com.sofka.cuentabancaria.service;

import static co.com.sofka.cuentabancaria.model.enums.TipoTransaccion.DEPOSITO_CAJERO;
import static co.com.sofka.cuentabancaria.model.enums.TipoTransaccion.RETIRO_CAJERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.repository.TransaccionRepository;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategy;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyFactory;
import co.com.sofka.cuentabancaria.service.strategy.enums.TipoOperacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class TransaccionServiceUnitTest {

    private TransaccionServiceImpl transaccionService;
    private TransaccionRepository transaccionRepository;
    private CuentaRepository cuentaRepository;
    private TransaccionStrategyFactory strategyFactory;
    @BeforeEach
    void setUp() {
        transaccionRepository = mock(TransaccionRepository.class);
        cuentaRepository = mock(CuentaRepository.class);
        strategyFactory = mock(TransaccionStrategyFactory.class);
        transaccionService = new TransaccionServiceImpl(transaccionRepository, cuentaRepository, strategyFactory);
    }

    @Test
    void testRealizarDeposito_Exitoso() {
        TransaccionRequestDTO depositoRequestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(1000), DEPOSITO_CAJERO);

        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(2000), "Juan Perez");
        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Mono.just(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(Mono.just(cuenta));

        TransaccionStrategy strategy = mock(TransaccionStrategy.class);
        when(strategy.getCosto()).thenReturn(BigDecimal.valueOf(10));
        when(strategyFactory.getStrategy(DEPOSITO_CAJERO, TipoOperacion.DEPOSITO)).thenReturn(strategy);

        Transaccion transaccion = new Transaccion(BigDecimal.valueOf(1000), BigDecimal.valueOf(10), LocalDateTime.now(),
                DEPOSITO_CAJERO, cuenta.getId());
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(Mono.just(transaccion));

        Mono<TransaccionResponseDTO> responseMono = transaccionService.realizarDeposito(depositoRequestDTO);

        StepVerifier.create(responseMono)
                .assertNext(responseDTO -> {
                    assertEquals(BigDecimal.valueOf(2990), responseDTO.getNuevoSaldo());
                })
                .verifyComplete();

        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }


    @Test
    void testRealizarRetiro_Exitoso() {
        TransaccionRequestDTO retiroRequestDTO = new TransaccionRequestDTO("9876543210", BigDecimal.valueOf(1000), RETIRO_CAJERO);

        // Crear cuenta con ID simulado
        Cuenta cuenta = new Cuenta("someId", "9876543210", BigDecimal.valueOf(5000), "Juan Perez");
        when(cuentaRepository.findByNumeroCuenta("9876543210")).thenReturn(Mono.just(cuenta));

        // Simula el comportamiento de la estrategia de transacción
        TransaccionStrategy strategy = mock(TransaccionStrategy.class);
        when(strategy.getCosto()).thenReturn(BigDecimal.valueOf(100));
        when(strategyFactory.getStrategy(RETIRO_CAJERO, TipoOperacion.RETIRO)).thenReturn(strategy);

        // Simula el comportamiento de save() en cuentaRepository
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(Mono.just(cuenta));

        // Simula el comportamiento de save() en transaccionRepository
        Transaccion transaccion = new Transaccion(BigDecimal.valueOf(1000), BigDecimal.valueOf(100), LocalDateTime.now(),
                RETIRO_CAJERO, cuenta.getId());
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(Mono.just(transaccion)); // Simula el save de transacción

        Mono<TransaccionResponseDTO> responseMono = transaccionService.realizarRetiro(retiroRequestDTO);

        // Verificamos la respuesta con StepVerifier
        StepVerifier.create(responseMono)
                .assertNext(responseDTO -> {
                    assertEquals(BigDecimal.valueOf(3900), responseDTO.getNuevoSaldo());
                })
                .verifyComplete(); // Verificamos que se haya completado el flujo
    }


    @Test
    void testRealizarDeposito_CuentaNoExiste() {
        TransaccionRequestDTO depositoRequestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(1000), DEPOSITO_CAJERO);

        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Mono.empty()); // Mono.empty para cuenta no encontrada

        Mono<TransaccionResponseDTO> responseMono = transaccionService.realizarDeposito(depositoRequestDTO);

        StepVerifier.create(responseMono)
                .expectError(NoSuchElementException.class) // Verificamos que se emita la excepción correcta
                .verify();
    }

    @Test
    void testRealizarRetiro_SaldoInsuficiente() {
        TransaccionRequestDTO retiroRequestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(1000), RETIRO_CAJERO);

        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(500), "Juan Perez");
        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Mono.just(cuenta));

        TransaccionStrategy strategy = mock(TransaccionStrategy.class);
        when(strategy.getCosto()).thenReturn(BigDecimal.valueOf(50));
        when(strategyFactory.getStrategy(RETIRO_CAJERO, TipoOperacion.RETIRO)).thenReturn(strategy);

        Mono<TransaccionResponseDTO> responseMono = transaccionService.realizarRetiro(retiroRequestDTO);

        StepVerifier.create(responseMono)
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof ConflictException);
                    assertEquals("Saldo insuficiente para realizar el retiro", throwable.getMessage());
                })
                .verify();
    }

}

