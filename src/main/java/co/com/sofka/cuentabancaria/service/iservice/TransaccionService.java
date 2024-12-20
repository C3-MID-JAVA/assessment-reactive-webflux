package co.com.sofka.cuentabancaria.service.iservice;


import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransaccionService {

    Flux<TransaccionResponseDTO> obtenerTransacciones();
    Mono<TransaccionResponseDTO> realizarDeposito(TransaccionRequestDTO transaccionRequestDTO);
    Mono<TransaccionResponseDTO> realizarRetiro(TransaccionRequestDTO transaccionRequestDTO);
    Flux<TransaccionResponseDTO> obtenerHistorialPorCuenta(String cuentaId);
}
