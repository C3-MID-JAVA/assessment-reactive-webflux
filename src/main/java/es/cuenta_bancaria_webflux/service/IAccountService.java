package es.cuenta_bancaria_webflux.service;
import es.cuenta_bancaria_webflux.dto.AccountDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface IAccountService {
    Flux<AccountDTO> listarCuentas();
    Mono<AccountDTO> crearCuenta(AccountDTO accountDTO);
    Mono<AccountDTO> obtenerCuentaPorId(String id);
    Mono<AccountDTO> realizarTransaccion(String cuentaId, BigDecimal monto, String tipo);
}
