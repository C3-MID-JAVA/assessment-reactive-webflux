package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.mapper.TransactionMapper;
import com.reactivo.banco.model.dto.TransactionInDTO;
import com.reactivo.banco.model.dto.TransactionOutDTO;
import com.reactivo.banco.model.entity.Transaction;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.TransactionRepository;
import com.reactivo.banco.service.TransactionService;
import com.reactivo.banco.util.TransactionCost;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionImplService implements TransactionService {

    private final TransactionRepository movimientoRepository;
    private final AccountRepository cuentaRepository;

    public TransactionImplService(TransactionRepository movimientoRepository, AccountRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }
    @Override
    public Mono<TransactionOutDTO> makeBranchDeposit(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.DEPOSITO_SUCURSAL.getCosto());
    }

    @Override
    public Mono<TransactionOutDTO> makeATMDeposit(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.DEPOSITO_CAJERO.getCosto());
    }

    @Override
    public Mono<TransactionOutDTO> makeDepositToAnotherAccount(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.DEPOSITO_OTRA_CUENTA.getCosto());
    }

    @Override
    public Mono<TransactionOutDTO> makePhysicalPurchase(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.COMPRA_FISICA.getCosto(), false);
    }

    @Override
    public Mono<TransactionOutDTO> makeOnlinePurchase(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.COMPRA_WEB.getCosto(), false);
    }

    @Override
    public Mono<TransactionOutDTO> makeATMWithdrawal(TransactionInDTO movimientoInDTO) {
        return realizarTransaccion(movimientoInDTO, TransactionCost.RETIRO_CAJERO.getCosto(), false);
    }

    private Mono<TransactionOutDTO> realizarTransaccion(TransactionInDTO movimientoInDTO, double costoTransaccion) {
        return realizarTransaccion(movimientoInDTO, costoTransaccion, true);
    }

    private Mono<TransactionOutDTO> realizarTransaccion(TransactionInDTO movimientoInDTO, double costoTransaccion, boolean esDeposito) {
        BigDecimal costoTransaccionBD = new BigDecimal(costoTransaccion);

        return cuentaRepository.findById(movimientoInDTO.getAccountId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + movimientoInDTO.getAccountId())))
                .flatMap(cuenta -> {
                    BigDecimal monto = movimientoInDTO.getAmount();
                    BigDecimal saldoAfectado = esDeposito
                            ? monto.subtract(costoTransaccionBD)
                            : monto.negate().subtract(costoTransaccionBD);

                    if (cuenta.getBalance().add(saldoAfectado).compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new ResourceNotFoundException("Saldo insuficiente para realizar la transacción."));
                    }

                    cuenta.setBalance(cuenta.getBalance().add(saldoAfectado));

                    return cuentaRepository.save(cuenta)
                            .thenReturn(saldoAfectado);
                })
                .flatMap(saldoAfectado -> {
                    Transaction movimiento = new Transaction();
                    movimiento.setDescription(movimientoInDTO.getDescription());
                    movimiento.setAmount(saldoAfectado.abs());
                    movimiento.setTransactionType(saldoAfectado.compareTo(BigDecimal.ZERO) < 0 ? "débito" : "crédito");
                    movimiento.setDate(LocalDate.now());
                    movimiento.setAccountId(movimientoInDTO.getAccountId());

                    return movimientoRepository.save(movimiento);
                })
                .map(TransactionMapper::toDTO);
    }
}
