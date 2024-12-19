package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CuentaServiceIntegrationTest {

    @Autowired
    private CuentaServiceImpl cuentaService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll().block(); // Bloquear para esperar la limpieza del repositorio.
    }

    @Test
    void testCrearCuenta_Exitoso() {
        CuentaRequestDTO requestDTO = new CuentaRequestDTO("0123456789", BigDecimal.valueOf(1000), "Juan Perez");

        Mono<CuentaResponseDTO> resultado = cuentaService.crearCuenta(requestDTO)
                .flatMap(responseDTO -> cuentaRepository.findById(responseDTO.getId())
                        .map(cuentaGuardada -> {
                            assertEquals("0123456789", cuentaGuardada.getNumeroCuenta());
                            assertEquals(BigDecimal.valueOf(1000), cuentaGuardada.getSaldo());
                            return responseDTO;
                        }));

        StepVerifier.create(resultado)
                .expectNextMatches(responseDTO -> responseDTO.getNumeroCuenta().equals("0123456789"))
                .verifyComplete();
    }

    @Test
    void testObtenerCuentaPorId_Exitoso() {
        Cuenta cuenta = new Cuenta("1234567890", BigDecimal.valueOf(500), "Juan Perez");
        cuentaRepository.save(cuenta).block(); // Bloqueo para asegurar que el dato se guarda antes de continuar.

        Mono<CuentaResponseDTO> resultado = cuentaService.obtenerCuentaPorId(cuenta.getId());

        StepVerifier.create(resultado)
                .assertNext(responseDTO -> {
                    assertEquals(cuenta.getNumeroCuenta(), responseDTO.getNumeroCuenta());
                    assertEquals(cuenta.getTitular(), responseDTO.getTitular());
                })
                .verifyComplete();
    }

    @Test
    void testObtenerCuentaPorId_NoExiste() {
        String cuentaIdInexistente = "99999";

        Mono<CuentaResponseDTO> resultado = cuentaService.obtenerCuentaPorId(cuentaIdInexistente);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof NoSuchElementException &&
                        throwable.getMessage().equals("No se encontro el cuenta con id: " + cuentaIdInexistente))
                .verify();
    }
}
