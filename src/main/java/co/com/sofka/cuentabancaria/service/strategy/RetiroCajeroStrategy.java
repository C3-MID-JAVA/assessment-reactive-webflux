package co.com.sofka.cuentabancaria.service.strategy;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.model.Cuenta;

import java.math.BigDecimal;

public class RetiroCajeroStrategy implements TransaccionStrategy{
    private static final BigDecimal COSTO = BigDecimal.valueOf(1.0);

    @Override
    public void validar(Cuenta cuenta, BigDecimal monto) {
        if (cuenta.getSaldo().compareTo(monto.add(COSTO)) < 0) {
            throw new ConflictException("Saldo insuficiente para retiro por cajero");
        }
    }

    @Override
    public BigDecimal getCosto() {
        return COSTO;
    }
}
