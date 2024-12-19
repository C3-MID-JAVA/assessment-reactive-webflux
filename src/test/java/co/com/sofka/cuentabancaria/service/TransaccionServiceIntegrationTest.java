package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.repository.TransaccionRepository;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategy;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyFactory;
import co.com.sofka.cuentabancaria.service.strategy.enums.TipoOperacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransaccionServiceIntegrationTest {


    private TransaccionServiceImpl transaccionService;
    private CuentaRepository cuentaRepository;
    private TransaccionRepository transaccionRepository;
    private TransaccionStrategyFactory strategyFactory;

    @BeforeEach
    void setUp() {
        cuentaRepository = mock(CuentaRepository.class);
        transaccionRepository = mock(TransaccionRepository.class);
        strategyFactory = mock(TransaccionStrategyFactory.class);
        transaccionService = new TransaccionServiceImpl(transaccionRepository,cuentaRepository, strategyFactory);
    }

    @Test
    void testRealizarDeposito_Exitoso() {
        Cuenta cuenta = new Cuenta("123", BigDecimal.valueOf(100000), "Juan Perez");
        TransaccionRequestDTO depositoRequestDTO = new TransaccionRequestDTO("123", BigDecimal.valueOf(100000), TipoTransaccion.DEPOSITO_CAJERO);

        TransaccionStrategy estrategiaMock = mock(TransaccionStrategy.class);
        BigDecimal costoTransaccion = BigDecimal.valueOf(2);

        when(estrategiaMock.getCosto()).thenReturn(costoTransaccion);
        when(strategyFactory.getStrategy(TipoTransaccion.DEPOSITO_CAJERO, TipoOperacion.DEPOSITO)).thenReturn(estrategiaMock);
        when(cuentaRepository.findByNumeroCuenta("123")).thenReturn(Mono.just(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> {
            Cuenta cuentaGuardada = invocation.getArgument(0);
            cuentaGuardada.setSaldo(cuentaGuardada.getSaldo().add(depositoRequestDTO.getMonto()).subtract(costoTransaccion)); // Actualiza el saldo
            return Mono.just(cuentaGuardada);
        });

        when(transaccionRepository.save(any(Transaccion.class)))
                .thenAnswer(invocation -> {
                    // Crear una transacción mock para la respuesta
                    Transaccion transaccion = invocation.getArgument(0);
                    transaccion.setId("mocked-id");
                    return Mono.just(transaccion);
                });

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarDeposito(depositoRequestDTO);

        StepVerifier.create(resultado)
                .assertNext(responseDTO -> {
                    BigDecimal saldoEsperado = cuenta.getSaldo().add(depositoRequestDTO.getMonto()).subtract(costoTransaccion);
                    assertEquals(saldoEsperado.setScale(0, RoundingMode.HALF_UP), responseDTO.getNuevoSaldo().setScale(0, RoundingMode.HALF_UP), "El saldo esperado no coincide");
                    assertNotNull(responseDTO);
                })
                .verifyComplete();
    }


    @Test
    void testRealizarRetiro_Exitoso() {
        // Configuración inicial
        Cuenta cuenta = new Cuenta("123", BigDecimal.valueOf(3000), "Juan Perez");
        TransaccionRequestDTO retiroRequestDTO = new TransaccionRequestDTO("123", BigDecimal.valueOf(1000), TipoTransaccion.RETIRO_CAJERO);
        TransaccionStrategy estrategiaMock = mock(TransaccionStrategy.class);
        BigDecimal costoTransaccion = BigDecimal.valueOf(1);

        // Mocks de comportamiento
        when(estrategiaMock.getCosto()).thenReturn(costoTransaccion);
        when(strategyFactory.getStrategy(TipoTransaccion.RETIRO_CAJERO, TipoOperacion.RETIRO)).thenReturn(estrategiaMock);
        when(cuentaRepository.findByNumeroCuenta("123")).thenReturn(Mono.just(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> {
            Cuenta cuentaActualizada = invocation.getArgument(0);
            return Mono.just(cuentaActualizada);
        });
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> {
            Transaccion transaccion = invocation.getArgument(0);
            transaccion.setId("mocked-id");
            return Mono.just(transaccion);
        });

        Mono<TransaccionResponseDTO> resultado = transaccionService.realizarRetiro(retiroRequestDTO);

        StepVerifier.create(resultado)
                .assertNext(responseDTO -> {
                    BigDecimal saldoEsperado = BigDecimal.valueOf(3000) // Saldo inicial
                            .subtract(retiroRequestDTO.getMonto())      // Monto del retiro
                            .subtract(costoTransaccion)                // Costo de la transacción
                            .setScale(2, RoundingMode.HALF_UP);        // Aseguramos precisión decimal

                    assertEquals(saldoEsperado, responseDTO.getNuevoSaldo().setScale(2, RoundingMode.HALF_UP), "El saldo nuevo no es el esperado");
                    assertNotNull(responseDTO.getId(), "El ID de la transacción no debe ser nulo");
                })
                .verifyComplete();
    }



    @Test
    void testObtenerHistorialPorCuenta() {
        // Crear una cuenta mock
        Cuenta cuenta = new Cuenta("123", BigDecimal.valueOf(300), "Juan Perez");

        // Crear transacciones mock (de tipo Transaccion)
        Transaccion transaccion1 = new Transaccion(BigDecimal.valueOf(100), BigDecimal.valueOf(2), LocalDateTime.now(), TipoTransaccion.DEPOSITO_CAJERO, "123");
        Transaccion transaccion2 = new Transaccion(BigDecimal.valueOf(200), BigDecimal.valueOf(1), LocalDateTime.now(), TipoTransaccion.RETIRO_CAJERO, "123");

        // Simular que la cuenta existe y tiene transacciones
        when(cuentaRepository.findById("123")).thenReturn(Mono.just(cuenta));
        when(transaccionRepository.findAllByCuentaId("123")).thenReturn(Flux.just(transaccion1, transaccion2));

        // Llamada al servicio para obtener el historial
        Flux<TransaccionResponseDTO> resultado = transaccionService.obtenerHistorialPorCuenta("123");

        // Verificación del resultado usando StepVerifier
        StepVerifier.create(resultado)
                .expectNextMatches(transaccion ->
                        transaccion.getMonto().equals(BigDecimal.valueOf(100)) &&
                                transaccion.getTipoTransaccion().equals(TipoTransaccion.DEPOSITO_CAJERO))
                .expectNextMatches(transaccion ->
                        transaccion.getMonto().equals(BigDecimal.valueOf(200)) &&
                        transaccion.getTipoTransaccion().equals(TipoTransaccion.RETIRO_CAJERO))
                .verifyComplete();
    }

}

