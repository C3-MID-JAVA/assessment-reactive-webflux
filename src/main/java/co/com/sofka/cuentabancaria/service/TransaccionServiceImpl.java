package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionRequestDTO;
import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.repository.TransaccionRepository;
import co.com.sofka.cuentabancaria.service.iservice.TransaccionService;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategy;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyContext;
import co.com.sofka.cuentabancaria.service.strategy.TransaccionStrategyFactory;
import co.com.sofka.cuentabancaria.service.strategy.enums.TipoOperacion;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionStrategyFactory strategyFactory;
    private static final Logger log = LoggerFactory.getLogger(TransaccionServiceImpl.class);

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


    private  Mono<TransaccionStrategyContext>  obtenerCuentaYStrategy(TransaccionRequestDTO requestDTO, TipoOperacion tipo) {

        return cuentaRepository.findByNumeroCuenta(requestDTO.getNumeroCuenta())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Cuenta no encontrada con el número de Cuenta: " + requestDTO.getNumeroCuenta())))
                .flatMap(cuenta -> {
                    TransaccionStrategy strategy = strategyFactory.getStrategy(requestDTO.getTipoTransaccion(), tipo);
                    BigDecimal monto = requestDTO.getMonto();
                    strategy.validar(cuenta, monto);
                    return Mono.just(new TransaccionStrategyContext(cuenta, strategy, monto));
                });
    }


    @Override
    public Mono<TransaccionResponseDTO> realizarDeposito(TransaccionRequestDTO depositoRequestDTO) {
        return obtenerCuentaYStrategy(depositoRequestDTO, TipoOperacion.DEPOSITO)
                .flatMap(context -> {
                    Cuenta cuenta = context.getCuenta();
                    TransaccionStrategy strategy = context.getStrategy();
                    BigDecimal monto = context.getMonto();
                    BigDecimal costoTransaccion = strategy.getCosto();
                    BigDecimal saldoFinal = cuenta.getSaldo().add(monto).subtract(costoTransaccion);

                    if (saldoFinal.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new ConflictException("Saldo insuficiente, tome en cuenta el costo de la transacción"));
                    }

                    cuenta.setSaldo(saldoFinal);
                    return cuentaRepository.save(cuenta)
                            .then(transaccionRepository.save(new Transaccion(monto, costoTransaccion, LocalDateTime.now(),
                                    depositoRequestDTO.getTipoTransaccion(), cuenta.getId())))
                            .map(transaccion -> new TransaccionResponseDTO(transaccion, cuenta.getSaldo(), cuenta.getNumeroCuenta()));
                });
    }

    @Override
    public Mono<TransaccionResponseDTO> realizarRetiro(TransaccionRequestDTO transaccionRequestDTO) {
        return obtenerCuentaYStrategy(transaccionRequestDTO, TipoOperacion.RETIRO)
                .flatMap(context -> {
                    Cuenta cuenta = context.getCuenta();
                    TransaccionStrategy strategy = context.getStrategy();
                    BigDecimal monto = context.getMonto();
                    BigDecimal costoTransaccion = strategy.getCosto();
                    BigDecimal montoConCosto = monto.add(costoTransaccion);

                    // Verificar saldo suficiente
                    if (cuenta.getSaldo().compareTo(montoConCosto) < 0) {
                        return Mono.error(new ConflictException("Saldo insuficiente para realizar el retiro"));
                    }

                    // Actualizar saldo
                    cuenta.setSaldo(cuenta.getSaldo().subtract(montoConCosto));

                    // Agregar log antes de guardar la cuenta con el saldo actualizado
                    log.info("Actualizando cuenta con nuevo saldo: {}", cuenta.getSaldo());

                    return Mono.defer(() ->
                            cuentaRepository.save(cuenta)  // Guardar la cuenta actualizada
                                    .flatMap(cuentaActualizada -> {
                                        // Log antes de guardar la transacción
                                        Transaccion transaccion = new Transaccion(
                                                monto, costoTransaccion, LocalDateTime.now(),
                                                transaccionRequestDTO.getTipoTransaccion(), cuentaActualizada.getId());
                                        log.info("Guardando transacción: {}", transaccion);

                                        // Guardar la transacción
                                        return transaccionRepository.save(transaccion)
                                                .map(transaccionGuardada -> new TransaccionResponseDTO(
                                                        transaccionGuardada, cuentaActualizada.getSaldo(), cuentaActualizada.getNumeroCuenta())
                                                );
                                    })
                    );
                });
    }




    @Override
    public Flux<TransaccionResponseDTO> obtenerHistorialPorCuenta(String cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId)
                .flatMap(this::mapearTransaccionAResponseDTO);
    }


    private Mono<TransaccionResponseDTO> mapearTransaccionAResponseDTO(Transaccion transaccion) {
            return cuentaRepository.findById(transaccion.getCuentaId())
                    .map(cuenta -> new TransaccionResponseDTO(transaccion, cuenta.getSaldo(), cuenta.getNumeroCuenta()))
                    .switchIfEmpty(Mono.just(new TransaccionResponseDTO(transaccion, BigDecimal.ZERO, "Cuenta desconocida")));
    }


}
