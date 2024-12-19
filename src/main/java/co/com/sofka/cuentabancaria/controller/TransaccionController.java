package co.com.sofka.cuentabancaria.controller;


import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.dto.util.PeticionByIdDTO;
import co.com.sofka.cuentabancaria.service.iservice.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/depositos")
    public Mono<ResponseEntity<TransaccionResponseDTO>> realizarDeposito(@Valid @RequestBody TransaccionRequestDTO depositoRequestDTO) {
        return transaccionService.realizarDeposito(depositoRequestDTO)
                .map(deposito -> ResponseEntity.status(HttpStatus.CREATED).body(deposito))
                .onErrorResume(IllegalArgumentException.class, ex ->
                        Mono.error(new IllegalArgumentException("Error al realizar el depósito: " + ex.getMessage()))
                );
    }

    @GetMapping("/listar")
    public Mono<ResponseEntity<Flux<TransaccionResponseDTO>>> listarTransacciones() {
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(transaccionService.obtenerTransacciones()));
    }

    @PostMapping("/retiro")
    public Mono<ResponseEntity<TransaccionResponseDTO>> realizarRetiro(@Valid @RequestBody TransaccionRequestDTO transaccionRequestDTO) {
        return transaccionService.realizarRetiro(transaccionRequestDTO)
                .map(retiro -> ResponseEntity.status(HttpStatus.OK).body(retiro))
                .onErrorResume(IllegalArgumentException.class, ex ->
                        Mono.error(new IllegalArgumentException("Error al realizar el retiro: " + ex.getMessage()))
                );
    }

    @PostMapping("/cuenta/historialById")
    public Mono<ResponseEntity<Flux<TransaccionResponseDTO>>> obtenerHistorialPorCuenta(@RequestBody PeticionByIdDTO cuentaRequestDTO) {
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(
                        transaccionService.obtenerHistorialPorCuenta(cuentaRequestDTO.getCuentaId())))
                .switchIfEmpty(Mono.error(new NoSuchElementException("No se encontró historial para la cuenta proporcionada.")));
    }

}
