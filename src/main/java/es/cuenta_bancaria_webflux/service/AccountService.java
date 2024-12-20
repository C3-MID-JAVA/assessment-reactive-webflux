package es.cuenta_bancaria_webflux.service;

import es.cuenta_bancaria_webflux.dto.AccountDTO;
import es.cuenta_bancaria_webflux.enums.TypeTransaction;
import es.cuenta_bancaria_webflux.exception.CuentaNoEncontradaException;
import es.cuenta_bancaria_webflux.exception.SaldoInsuficienteException;
import es.cuenta_bancaria_webflux.mapper.AccountMapper;
import es.cuenta_bancaria_webflux.model.Account;
import es.cuenta_bancaria_webflux.model.Transaction;
import es.cuenta_bancaria_webflux.repository.AccountRepository;
import es.cuenta_bancaria_webflux.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import es.cuenta_bancaria_webflux.utils.ConfirmationMessageGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Service
public class AccountService implements IAccountService{
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper dtoMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper dtoMapper, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.dtoMapper = dtoMapper;
        this.transactionRepository = transactionRepository;
    }

    public Flux<AccountDTO> listarCuentas() {
        return accountRepository.findAll()
                .map(dtoMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    public Mono<AccountDTO> crearCuenta(AccountDTO accountDTO) {
        Account account = dtoMapper.toEntity(accountDTO);
        return accountRepository.save(account)
                .map(dtoMapper::toDto)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al crear la cuenta: " + e.getMessage())));
    }

    public Mono<AccountDTO> obtenerCuentaPorId(String id) {
        return accountRepository.findById(id)
                .map(dtoMapper::toDto)
                .switchIfEmpty(Mono.error(new CuentaNoEncontradaException("Cuenta no encontrada")));
    }

    public Mono<AccountDTO> realizarTransaccion(String cuentaId, BigDecimal monto, String tipo) {
        return accountRepository.findById(cuentaId)
                .switchIfEmpty(Mono.error(new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + cuentaId)))
                .flatMap(cuenta -> {


                    TypeTransaction tipoTransaccion;
                    if (!TypeTransaction.validadorTipo.validar(tipo)) {
                        return Mono.error(new IllegalArgumentException("Tipo de transacción no válido: " + tipo));
                    } else {
                        tipoTransaccion = TypeTransaction.fromString(tipo);
                    }

                    BigDecimal costo = tipoTransaccion.getCosto();
                    /*
                    BigDecimal totalADescontar = monto.add(costo);
                    if ((tipo.startsWith("RETIRO") || tipo.equals("COMPRA_WEB")) &&
                            cuenta.getSaldo().compareTo(totalADescontar) < 0) {
                        return Mono.error(new SaldoInsuficienteException("Saldo insuficiente para realizar esta transacción"));
                    }

                    // Actualizar saldo
                    cuenta.setSaldo(cuenta.getSaldo().subtract(totalADescontar));
                    */
                    if (tipo.startsWith("RETIRO") || tipo.equals("COMPRA_WEB") || tipo.equals("COMPRA_ESTABLECIMIENTO")) {
                        BigDecimal totalADescontar = monto.add(costo);

                        if (cuenta.getSaldo().compareTo(totalADescontar) < 0) {
                            throw new SaldoInsuficienteException("Saldo insuficiente para realizar esta transacción");
                        }

                        cuenta.setSaldo(cuenta.getSaldo().subtract(totalADescontar));
                    } else {
                        cuenta.setSaldo(cuenta.getSaldo().add(monto).subtract(costo)); // Descontar costo del depósito
                    }
                    // Crear y guardar transacción
                    Transaction transaccion = new Transaction();
                    transaccion.setMonto(monto);
                    transaccion.setTipo(tipo);
                    transaccion.setCosto(costo);
                    transaccion.setIdCuenta(cuentaId);

                    /*return transactionRepository.save(transaccion)
                            .then(accountRepository.save(cuenta))
                            .map(dtoMapper::toDto);*/

                    return transactionRepository.save(transaccion)
                            .flatMap(savedTransaccion -> {
                                Supplier<String> confirmationMessage = ConfirmationMessageGenerator.createConfirmationMessage(savedTransaccion);
                                System.out.println(confirmationMessage.get());
                                return accountRepository.save(cuenta).map(dtoMapper::toDto);
                            });
                });
    }
}
