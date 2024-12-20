package co.com.sofka.cuentabancaria.service.iservice;

import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CuentaService {
    Flux<CuentaResponseDTO> obtenerCuentas(); // Devuelve un flujo de cuentas
    Mono<CuentaResponseDTO> crearCuenta(CuentaRequestDTO cuentaRequestDTO); // Devuelve una cuenta creada
    Mono<CuentaResponseDTO> obtenerCuentaPorId(String id); // Devuelve una cuenta específica
    Mono<BigDecimal> consultarSaldo(String id); // Devuelve el saldo como un Mono
}
