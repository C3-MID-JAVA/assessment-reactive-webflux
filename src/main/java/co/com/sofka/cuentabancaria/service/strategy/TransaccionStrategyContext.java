package co.com.sofka.cuentabancaria.service.strategy;

import co.com.sofka.cuentabancaria.model.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransaccionStrategyContext {
    private final Cuenta cuenta;
    private final TransaccionStrategy strategy;
    private final BigDecimal monto;


}
