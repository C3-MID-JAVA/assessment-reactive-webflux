package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class CuentaServiceUnitTest {

    private CuentaServiceImpl cuentaService;
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        cuentaRepository = mock(CuentaRepository.class);
        cuentaService = new CuentaServiceImpl(cuentaRepository);
    }

    @Test
    void testCrearCuenta_Exitoso() {
        CuentaRequestDTO requestDTO = new CuentaRequestDTO("12345", BigDecimal.valueOf(1000), "Juan Perez");
        Cuenta cuenta = new Cuenta("675dbabe03edcf54111957fe", "12345", BigDecimal.valueOf(1000), "Juan Perez");

        when(cuentaRepository.findByNumeroCuenta("12345")).thenReturn(Mono.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(Mono.just(cuenta));

        Mono<CuentaResponseDTO> resultado = cuentaService.crearCuenta(requestDTO);

        StepVerifier.create(resultado)
                .assertNext(responseDTO -> {
                    assertEquals("12345", responseDTO.getNumeroCuenta());
                    assertEquals(BigDecimal.valueOf(1000), responseDTO.getSaldo());
                })
                .verifyComplete();

        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testCrearCuenta_CuentaExistente() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO("0123456789", BigDecimal.valueOf(1000), "Juan Perez");

        when(cuentaRepository.findByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta()))
                .thenReturn(Mono.just(new Cuenta()));

        Mono<CuentaResponseDTO> resultado = cuentaService.crearCuenta(cuentaRequestDTO);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("El número de cuenta ya está registrado."))
                .verify();
    }

    @Test
    void testObtenerCuentaPorId_Exitoso() {
        Cuenta cuenta = new Cuenta("675dbabe03edcf54111957fe", "0123456789", BigDecimal.valueOf(1000), "Juan Perez");

        when(cuentaRepository.findById("675dbabe03edcf54111957fe")).thenReturn(Mono.just(cuenta));

        Mono<CuentaResponseDTO> resultado = cuentaService.obtenerCuentaPorId("675dbabe03edcf54111957fe");

        StepVerifier.create(resultado)
                .assertNext(responseDTO -> {
                    assertEquals("0123456789", responseDTO.getNumeroCuenta());
                    assertEquals("Juan Perez", responseDTO.getTitular());
                })
                .verifyComplete();

        verify(cuentaRepository, times(1)).findById("675dbabe03edcf54111957fe");
    }

    @Test
    void testObtenerCuentaPorId_NoExiste() {
        when(cuentaRepository.findById("12345")).thenReturn(Mono.empty());

        Mono<CuentaResponseDTO> resultado = cuentaService.obtenerCuentaPorId("12345");

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof NoSuchElementException &&
                        throwable.getMessage().equals("No se encontro el cuenta con id: 12345"))
                .verify();
    }

    @Test
    void testCrearCuenta_CuentaNueva() {
        CuentaRequestDTO cuentaRequestDTO = new CuentaRequestDTO("123456", BigDecimal.valueOf(1000), "Juan Pérez");
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setId("1");
        cuentaMock.setNumeroCuenta(cuentaRequestDTO.getNumeroCuenta());
        cuentaMock.setTitular(cuentaRequestDTO.getTitular());
        cuentaMock.setSaldo(cuentaRequestDTO.getSaldoInicial());

        when(cuentaRepository.findByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())).thenReturn(Mono.empty());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(Mono.just(cuentaMock));

        Mono<CuentaResponseDTO> resultado = cuentaService.crearCuenta(cuentaRequestDTO);

        StepVerifier.create(resultado)
                .assertNext(cuentaResponseDTO -> {
                    assertNotNull(cuentaResponseDTO);
                    assertEquals("1", cuentaResponseDTO.getId());
                    assertEquals("123456", cuentaResponseDTO.getNumeroCuenta());
                })
                .verifyComplete();
    }
}
