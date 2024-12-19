package co.com.sofka.cuentabancaria.controller;

import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.dto.util.PeticionByIdDTO;
import co.com.sofka.cuentabancaria.service.iservice.TransaccionService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class TransaccionControllerTest {

    @Mock
    private TransaccionService transaccionService;

    @InjectMocks
    private TransaccionController transaccionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void realizarDeposito_Exito() {
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        TransaccionResponseDTO response = new TransaccionResponseDTO();

        when(transaccionService.realizarDeposito(request)).thenReturn(Mono.just(response));

        Mono<ResponseEntity<TransaccionResponseDTO>> resultado = transaccionController.realizarDeposito(request);

        StepVerifier.create(resultado)
                .assertNext(entity -> {
                    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
                    assertEquals(response, entity.getBody());
                })
                .verifyComplete();

        verify(transaccionService, times(1)).realizarDeposito(request);
    }

    @Test
    void realizarDeposito_Fallo_Validacion() {
        TransaccionRequestDTO request = null;

        when(transaccionService.realizarDeposito(request))
                .thenReturn(Mono.error(new IllegalArgumentException("El cuerpo de la solicitud no puede ser nulo")));

        Mono<ResponseEntity<TransaccionResponseDTO>> resultado = transaccionController.realizarDeposito(request);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El cuerpo de la solicitud no puede ser nulo"))
                .verify();

        verify(transaccionService, times(1)).realizarDeposito(request);
    }

    @Test
    void listarTransacciones_Exito() {
        List<TransaccionResponseDTO> response = Arrays.asList(new TransaccionResponseDTO(), new TransaccionResponseDTO());

        when(transaccionService.obtenerTransacciones()).thenReturn(Flux.fromIterable(response));

        Mono<ResponseEntity<Flux<TransaccionResponseDTO>>> resultado = transaccionController.listarTransacciones();

        StepVerifier.create(resultado.flatMapMany(ResponseEntity::getBody))
                .expectNext(response.get(0))
                .expectNext(response.get(1))
                .verifyComplete();

        verify(transaccionService, times(1)).obtenerTransacciones();
    }

    @Test
    void realizarRetiro_Exito() {
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        TransaccionResponseDTO response = new TransaccionResponseDTO();

        when(transaccionService.realizarRetiro(request)).thenReturn(Mono.just(response));

        Mono<ResponseEntity<TransaccionResponseDTO>> resultado = transaccionController.realizarRetiro(request);

        StepVerifier.create(resultado)
                .assertNext(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(response, entity.getBody());
                })
                .verifyComplete();

        verify(transaccionService, times(1)).realizarRetiro(request);
    }

    @Test
    void obtenerHistorialPorCuenta_Exito() {
        PeticionByIdDTO peticion = new PeticionByIdDTO("12345");
        List<TransaccionResponseDTO> response = Arrays.asList(new TransaccionResponseDTO(), new TransaccionResponseDTO());

        when(transaccionService.obtenerHistorialPorCuenta(peticion.getCuentaId()))
                .thenReturn(Flux.fromIterable(response));

        Mono<ResponseEntity<Flux<TransaccionResponseDTO>>> resultado = transaccionController.obtenerHistorialPorCuenta(peticion);

        StepVerifier.create(resultado.flatMapMany(ResponseEntity::getBody))
                .expectNext(response.get(0))
                .expectNext(response.get(1))
                .verifyComplete();

        verify(transaccionService, times(1)).obtenerHistorialPorCuenta(peticion.getCuentaId());
    }

    @Test
    void obtenerHistorialPorCuenta_CuentaNoExiste() {
        PeticionByIdDTO peticion = new PeticionByIdDTO("cuenta_invalida");

        when(transaccionService.obtenerHistorialPorCuenta(peticion.getCuentaId()))
                .thenReturn(Flux.empty());

        Mono<ResponseEntity<Flux<TransaccionResponseDTO>>> resultado = transaccionController.obtenerHistorialPorCuenta(peticion);

        StepVerifier.create(resultado.flatMapMany(ResponseEntity::getBody))
                .verifyComplete();

        verify(transaccionService, times(1)).obtenerHistorialPorCuenta(peticion.getCuentaId());
    }
}
