package co.com.sofka.cuentabancaria.service.strategy;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.model.Cuenta;

import java.math.BigDecimal;

public interface TransaccionStrategy {
    void validar(Cuenta cuenta, BigDecimal monto) throws ConflictException;

    BigDecimal getCosto();
}
