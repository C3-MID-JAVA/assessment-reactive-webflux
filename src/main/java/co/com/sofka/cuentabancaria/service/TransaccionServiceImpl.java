package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.repository.TransaccionRepository;
import co.com.sofka.cuentabancaria.service.iservice.TransaccionService;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategy;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyContext;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyFactory;
import co.com.sofka.cuentabancaria.service.strategy.enums.TipoOperacion;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionStrategyFactory strategyFactory;
    private final Predicate<BigDecimal> isSaldoInsuficiente = saldo -> saldo.compareTo(BigDecimal.ZERO) < 0;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository, TransaccionStrategyFactory strategyFactory) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public Flux<TransaccionResponseDTO> obtenerTransacciones() {

        return transaccionRepository.findAll()
                .flatMap(this::mapearTransaccionAResponseDTO);
    }

    @Override
    public Mono<TransaccionResponseDTO> realizarDeposito(TransaccionRequestDTO depositoRequestDTO) {
        return procesarTransaccion(depositoRequestDTO, TipoOperacion.DEPOSITO);
    }

    @Override
    public Mono<TransaccionResponseDTO> realizarRetiro(TransaccionRequestDTO retiroRequestDTO) {
        return procesarTransaccion(retiroRequestDTO, TipoOperacion.RETIRO);
    }

    @Override
    public Flux<TransaccionResponseDTO> obtenerHistorialPorCuenta(String cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId)
                .flatMap(this::mapearTransaccionAResponseDTO);
    }

    private  Mono<TransaccionStrategyContext>  obtenerCuentaYStrategy(TransaccionRequestDTO requestDTO, TipoOperacion tipo) {

        return cuentaRepository.findByNumeroCuenta(requestDTO.getNumeroCuenta())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Cuenta no encontrada con el número de Cuenta: " + requestDTO.getNumeroCuenta())))
                .flatMap(cuenta -> {
                    TransaccionStrategy strategy = strategyFactory.getStrategy(requestDTO.getTipoTransaccion(), tipo);
                    strategy.validar(cuenta,requestDTO.getMonto());
                    return Mono.just(new TransaccionStrategyContext(cuenta, strategy, requestDTO.getMonto()));
                });
    }

    private Mono<TransaccionResponseDTO> procesarTransaccion(TransaccionRequestDTO requestDTO, TipoOperacion tipoOperacion) {
        return obtenerCuentaYStrategy(requestDTO, tipoOperacion)
                .flatMap(context -> {
                    Cuenta cuenta = context.getCuenta();
                    TransaccionStrategy strategy = context.getStrategy();
                    BigDecimal monto = context.getMonto();
                    BigDecimal costoTransaccion = strategy.getCosto();
                    BigDecimal saldoFinal = calcularSaldo(cuenta.getSaldo(), monto, costoTransaccion, tipoOperacion);

                    if (isSaldoInsuficiente.test(saldoFinal)) {
                        return Mono.error(new ConflictException("Saldo insuficiente para realizar la operación."));
                    }

                    cuenta.setSaldo(saldoFinal);
                    return guardarCuentaYTransaccion(cuenta, monto, costoTransaccion, requestDTO.getTipoTransaccion());
                });
    }


    private BigDecimal calcularSaldo(BigDecimal saldoActual, BigDecimal monto, BigDecimal costo, TipoOperacion tipo) {
        return tipo == TipoOperacion.DEPOSITO
                ? saldoActual.add(monto).subtract(costo)
                : saldoActual.subtract(monto.add(costo));
    }

    private Mono<TransaccionResponseDTO> guardarCuentaYTransaccion(Cuenta cuenta, BigDecimal monto, BigDecimal costo, TipoTransaccion tipoTransaccion) {
        return cuentaRepository.save(cuenta)
                .flatMap(cuentaGuardada -> {
                    Transaccion transaccion = new Transaccion(monto, costo, LocalDateTime.now(), tipoTransaccion, cuentaGuardada.getId());
                    return transaccionRepository.save(transaccion)
                            .map(transaccionGuardada -> new TransaccionResponseDTO(transaccionGuardada, cuentaGuardada.getSaldo(), cuentaGuardada.getNumeroCuenta()));
                });
    }

    private Mono<TransaccionResponseDTO> mapearTransaccionAResponseDTO(Transaccion transaccion) {
            return cuentaRepository.findById(transaccion.getCuentaId())
                    .map(cuenta -> new TransaccionResponseDTO(transaccion, cuenta.getSaldo(), cuenta.getNumeroCuenta()))
                    .switchIfEmpty(Mono.just(new TransaccionResponseDTO(transaccion, BigDecimal.ZERO, "Cuenta desconocida")));
    }

}
