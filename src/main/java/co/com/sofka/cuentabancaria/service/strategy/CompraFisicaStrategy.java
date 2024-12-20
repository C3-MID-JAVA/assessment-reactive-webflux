package co.com.sofka.cuentabancaria.service.strategy;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.model.Cuenta;

import java.math.BigDecimal;

public class CompraFisicaStrategy implements TransaccionStrategy {
    private static final BigDecimal COSTO = BigDecimal.ZERO;

    @Override
    public void validar(Cuenta cuenta, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("El monto para una compra física debe ser mayor a 0.");
        }

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new ConflictException("Saldo insuficiente para realizar la compra física.");
        }
    }

    @Override
    public BigDecimal getCosto() {
        return COSTO;
    }
}

