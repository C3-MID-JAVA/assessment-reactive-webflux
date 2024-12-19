package co.com.sofka.cuentabancaria.controller;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import co.com.sofka.cuentabancaria.dto.util.PeticionByIdDTO;
import co.com.sofka.cuentabancaria.service.CuentaServiceImpl;
import co.com.sofka.cuentabancaria.service.iservice.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;


    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public Mono<ResponseEntity<CuentaResponseDTO>> crearCuenta(@Valid @RequestBody CuentaRequestDTO cuentaRequestDTO) {

        return cuentaService.crearCuenta(cuentaRequestDTO)
                .map(cuentaResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(cuentaResponseDTO));
    }

    @GetMapping("/listar")
    public Mono<ResponseEntity<Flux<CuentaResponseDTO>>> obtenerCuentas() {

        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(cuentaService.obtenerCuentas()));
    }

    @PostMapping("/listarById")
    public Mono<ResponseEntity<CuentaResponseDTO>> obtenerCuentaPorId(@RequestBody PeticionByIdDTO cuentaRequestDTO) {

        return cuentaService.obtenerCuentaPorId(cuentaRequestDTO.getCuentaId())
                .map(cuentaResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(cuentaResponseDTO));
    }

    @PostMapping("/listar/saldoById")
    public Mono<ResponseEntity<BigDecimal>> consultarSaldo(@RequestBody PeticionByIdDTO cuentaRequestDTO) {
        return cuentaService.consultarSaldo(cuentaRequestDTO.getCuentaId())
                .map(saldo -> ResponseEntity.status(HttpStatus.OK).body(saldo));
    }
}