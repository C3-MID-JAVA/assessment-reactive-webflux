package co.com.sofka.cuentabancaria.controller;

import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import co.com.sofka.cuentabancaria.dto.util.PeticionByIdDTO;
import co.com.sofka.cuentabancaria.service.iservice.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCuenta() {
        CuentaRequestDTO requestDTO = new CuentaRequestDTO("1234567890", BigDecimal.valueOf(1000), "Anderson");
        CuentaResponseDTO responseDTO = new CuentaResponseDTO("1", "1234567890", BigDecimal.valueOf(1000), "Zambrano");

        when(cuentaService.crearCuenta(requestDTO)).thenReturn(Mono.just(responseDTO));
        Mono<ResponseEntity<CuentaResponseDTO>> response = cuentaController.crearCuenta(requestDTO);
        StepVerifier.create(response)
                .assertNext(entity -> {
                    assertNotNull(entity);
                    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
                    assertEquals(responseDTO, entity.getBody());
                })
                .verifyComplete();
        verify(cuentaService, times(1)).crearCuenta(requestDTO);
    }

    @Test
    void testSaveCuentaDatosInvalidos() {
        CuentaRequestDTO requestDTO = new CuentaRequestDTO(null, BigDecimal.valueOf(-1000), null);

        when(cuentaService.crearCuenta(requestDTO)).thenReturn(Mono.error(new IllegalArgumentException("Datos inválidos")));
        Mono<ResponseEntity<CuentaResponseDTO>> response = cuentaController.crearCuenta(requestDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Datos inválidos"))
                .verify();
        verify(cuentaService, times(1)).crearCuenta(requestDTO);
    }

    @Test
    void testObtenerCuentas() {
        List<CuentaResponseDTO> cuentasMock = Arrays.asList(
                new CuentaResponseDTO("1", "1234567890", BigDecimal.valueOf(1000), "Cristóbal"),
                new CuentaResponseDTO("2", "1234567891", BigDecimal.valueOf(2000), "Balseca")
        );

        when(cuentaService.obtenerCuentas()).thenReturn(Flux.fromIterable(cuentasMock));

        Mono<ResponseEntity<Flux<CuentaResponseDTO>>> response = cuentaController.obtenerCuentas();

        StepVerifier.create(response.flatMapMany(ResponseEntity::getBody))
                .expectNext(cuentasMock.get(0))
                .expectNext(cuentasMock.get(1))
                .verifyComplete();

        verify(cuentaService, times(1)).obtenerCuentas();
    }

    @Test
    void testConsultarSaldoCuentaInexistente() {
        PeticionByIdDTO peticion = new PeticionByIdDTO("999");

        when(cuentaService.consultarSaldo("999")).thenReturn(Mono.error(new RuntimeException("Cuenta no encontrada")) );

        Mono<ResponseEntity<BigDecimal>> response = cuentaController.consultarSaldo(peticion);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Cuenta no encontrada"))
                .verify();
        verify(cuentaService, times(1)).consultarSaldo("999");
    }

    @Test
    void testObtenerCuentaPorId() {
        CuentaResponseDTO cuentaMock = new CuentaResponseDTO("1", "1234567890", BigDecimal.valueOf(1000), "Cristhian");
        PeticionByIdDTO peticion = new PeticionByIdDTO("1");

        when(cuentaService.obtenerCuentaPorId("1")).thenReturn(Mono.just(cuentaMock));

        Mono<ResponseEntity<CuentaResponseDTO>> response = cuentaController.obtenerCuentaPorId(peticion);

        StepVerifier.create(response)
                        .assertNext(entity -> {
                            assertNotNull(entity);
                            assertEquals(HttpStatus.OK, entity.getStatusCode());
                            assertEquals(cuentaMock, entity.getBody());
                        });

        verify(cuentaService, times(1)).obtenerCuentaPorId("1");
    }

    @Test
    void testConsultarSaldo() {
        BigDecimal saldoMock = BigDecimal.valueOf(1500);
        PeticionByIdDTO peticion = new PeticionByIdDTO("1");

        when(cuentaService.consultarSaldo("1")).thenReturn(Mono.just(saldoMock));

        Mono<ResponseEntity<BigDecimal>> response  = cuentaController.consultarSaldo(peticion);

        StepVerifier.create(response)
                        .assertNext(entity -> {
                            assertNotNull(entity);
                            assertEquals(HttpStatus.OK, entity.getStatusCode());
                            assertEquals(saldoMock, entity.getBody());
                        });

        verify(cuentaService, times(1)).consultarSaldo("1");
    }

    @Test
    void testObtenerCuentaPorIdInvalido() {
        PeticionByIdDTO peticion = new PeticionByIdDTO("invalid_id");

        when(cuentaService.obtenerCuentaPorId("invalid_id")).thenReturn(Mono.error(new IllegalArgumentException("ID inválido")));

        Mono<ResponseEntity<CuentaResponseDTO>> response = cuentaController.obtenerCuentaPorId(peticion);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("ID inválido"))
                .verify();

        verify(cuentaService, times(1)).obtenerCuentaPorId("invalid_id");
    }

}
