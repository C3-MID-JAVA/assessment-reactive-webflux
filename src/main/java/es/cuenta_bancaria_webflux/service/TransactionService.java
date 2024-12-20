package es.cuenta_bancaria_webflux.service;
import es.cuenta_bancaria_webflux.dto.TransactionDTO;
import es.cuenta_bancaria_webflux.exception.CuentaNoEncontradaException;
import es.cuenta_bancaria_webflux.mapper.TransactionMapper;
import es.cuenta_bancaria_webflux.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService implements ITransactionService{
    private final TransactionRepository transactionRepository;
    private final TransactionMapper dtoMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper dtoMapper) {
        this.transactionRepository = transactionRepository;
        this.dtoMapper = dtoMapper;
    }

    public Flux<TransactionDTO> listarTransacciones() {
        return transactionRepository.findAll()
                .map(dtoMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    public Mono<TransactionDTO> obtenerTransaccionPorId(String id) {
        return transactionRepository.findById(id)
                .map(dtoMapper::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaccion no encontrada")));
    }

    @Override
    public Flux<TransactionDTO> obtenerTransaccionesPorCuenta(String idCuenta) {
        return transactionRepository.findByIdCuenta(idCuenta)
                .map(dtoMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }
}
