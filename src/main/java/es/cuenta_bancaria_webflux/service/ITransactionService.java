package es.cuenta_bancaria_webflux.service;



import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
public interface ITransactionService {
    Flux<TransactionDTO> listarTransacciones();
    Mono<TransactionDTO> obtenerTransaccionPorId(String id);
    Flux<TransactionDTO> obtenerTransaccionesPorCuenta(String id);

}
