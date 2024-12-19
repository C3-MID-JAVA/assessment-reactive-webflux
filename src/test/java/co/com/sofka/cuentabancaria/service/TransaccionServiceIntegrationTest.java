package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransaccionServiceIntegrationTest {

    @Autowired
    private TransaccionServiceImpl transaccionService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll().block();
        transaccionRepository.deleteAll().block();
    }

    // Test para realizarDeposito - caso positivo
    @Test
    void testRealizarDepositoExitoso() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(1000), "Juan Perez");
        cuentaRepository.save(cuenta).block();

        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(500), TipoTransaccion.DEPOSITO_CAJERO);

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarDeposito(requestDTO);

        StepVerifier.create(resultado)
                .assertNext(response -> {
                    assertEquals("1234567890", response.getNumeroCuenta());
                    assertEquals(BigDecimal.valueOf(1498.0), response.getNuevoSaldo());
                })
                .verifyComplete();
    }

    // Test para realizarDeposito - caso negativo
    @Test
    void testRealizarDepositoCuentaNoEncontrada() {
        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO("99999", BigDecimal.valueOf(500), TipoTransaccion.DEPOSITO_CAJERO);

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarDeposito(requestDTO);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof NoSuchElementException &&
                        throwable.getMessage().equals("Cuenta no encontrada con el número de Cuenta: 99999"))
                .verify();
    }

    // Test para realizarRetiro - caso positivo
    @Test
    void testRealizarRetiroExitoso() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(1000), "Juan Perez");
        cuentaRepository.save(cuenta).block();

        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(500), TipoTransaccion.COMPRA_EN_LINEA);

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarRetiro(requestDTO);

        StepVerifier.create(resultado)
                .assertNext(response -> {
                    assertEquals("1234567890", response.getNumeroCuenta());
                    assertEquals(BigDecimal.valueOf(495.0), response.getNuevoSaldo());
                })
                .verifyComplete();
    }

    // Test para realizarRetiro - caso negativo
    @Test
    void testRealizarRetiroSaldoInsuficiente() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(100), "Juan Perez");
        cuentaRepository.save(cuenta).block();

        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO("1234567890", BigDecimal.valueOf(500), TipoTransaccion.COMPRA_EN_LINEA);

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarRetiro(requestDTO);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Saldo insuficiente para compra en línea"))
                .verify();
    }

    // Test para obtenerHistorialPorCuenta - caso positivo
    @Test
    void testObtenerHistorialPorCuentaExitoso() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(1000), "Juan Perez");
        cuentaRepository.save(cuenta).block();

        Transaccion transaccion1 = new Transaccion(BigDecimal.valueOf(200), BigDecimal.valueOf(5), LocalDateTime.now(), TipoTransaccion.DEPOSITO_CAJERO, cuenta.getId());
        Transaccion transaccion2 = new Transaccion(BigDecimal.valueOf(100), BigDecimal.valueOf(2), LocalDateTime.now(), TipoTransaccion.COMPRA_EN_LINEA, cuenta.getId());
        transaccionRepository.saveAll(Flux.just(transaccion1, transaccion2)).blockLast();

        Flux<TransaccionResponseDTO> resultado = transaccionService.obtenerHistorialPorCuenta(cuenta.getId());

        StepVerifier.create(resultado)
                .expectNextCount(2)
                .verifyComplete();
    }

    // Test para obtenerHistorialPorCuenta - caso negativo
    @Test
    void testObtenerHistorialPorCuentaNoExiste() {
        Flux<TransaccionResponseDTO> resultado = transaccionService.obtenerHistorialPorCuenta("99999");

        StepVerifier.create(resultado)
                .expectNextCount(0)
                .verifyComplete();
    }

    // Test para obtenerTransacciones - caso positivo
    @Test
    void testObtenerTransaccionesExitoso() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(1000), "Juan Perez");
        cuentaRepository.save(cuenta).block();

        Transaccion transaccion1 = new Transaccion(BigDecimal.valueOf(200), BigDecimal.valueOf(5), LocalDateTime.now(), TipoTransaccion.DEPOSITO_CAJERO, cuenta.getId());
        Transaccion transaccion2 = new Transaccion(BigDecimal.valueOf(100), BigDecimal.valueOf(2), LocalDateTime.now(), TipoTransaccion.COMPRA_EN_LINEA, cuenta.getId());
        transaccionRepository.saveAll(Flux.just(transaccion1, transaccion2)).blockLast();

        Flux<TransaccionResponseDTO> resultado = transaccionService.obtenerTransacciones();

        StepVerifier.create(resultado)
                .expectNextCount(2)
                .verifyComplete();
    }

    // Test para obtenerTransacciones - caso negativo (sin transacciones)
    @Test
    void testObtenerTransaccionesSinDatos() {
        Flux<TransaccionResponseDTO> resultado = transaccionService.obtenerTransacciones();

        StepVerifier.create(resultado)
                .expectNextCount(0)
                .verifyComplete();
    }
}
