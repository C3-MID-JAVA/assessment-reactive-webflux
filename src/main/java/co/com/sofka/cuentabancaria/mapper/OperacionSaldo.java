package co.com.sofka.cuentabancaria.mapper;

import java.math.BigDecimal;

@FunctionalInterface
public interface OperacionSaldo {
    BigDecimal calcular(BigDecimal saldoActual, BigDecimal monto, BigDecimal costo);
}
