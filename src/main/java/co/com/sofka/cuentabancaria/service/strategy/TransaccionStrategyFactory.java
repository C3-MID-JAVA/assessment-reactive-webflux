package co.com.sofka.cuentabancaria.service.strategy;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import co.com.sofka.cuentabancaria.service.strategy.enums.TipoOperacion;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
@Component
public class TransaccionStrategyFactory {
    private final Map<TipoTransaccion, TransaccionStrategy> strategies = new EnumMap<>(TipoTransaccion.class);

    public TransaccionStrategyFactory() {
        strategies.put(TipoTransaccion.DEPOSITO_CAJERO, new DepositoCajeroStrategy());
        strategies.put(TipoTransaccion.DEPOSITO_OTRA_CUENTA, new DepositoOtraCuentaStrategy());
        strategies.put(TipoTransaccion.DEPOSITO_SUCURSAL, new DepositoSucursalStrategy());
        strategies.put(TipoTransaccion.RETIRO_CAJERO, new RetiroCajeroStrategy());
        strategies.put(TipoTransaccion.COMPRA_EN_LINEA, new CompraEnLineaStrategy());
        strategies.put(TipoTransaccion.COMPRA_FISICA, new CompraFisicaStrategy());
    }

    public TransaccionStrategy getStrategy(TipoTransaccion tipoTransaccion, TipoOperacion tipoOperacion) {
        if (tipoOperacion == TipoOperacion.DEPOSITO) {
            if (esTransaccionValidaDeposito(tipoTransaccion)) {
                return strategies.get(tipoTransaccion);
            }
        } else if (tipoOperacion == TipoOperacion.RETIRO) {
            if (esTransaccionValidaRetiro(tipoTransaccion)) {
                return strategies.get(tipoTransaccion);
            }
        }

        throw new ConflictException("Tipo de transacción no válido para la operación " + tipoOperacion);
    }

    private boolean esTransaccionValidaDeposito(TipoTransaccion tipoTransaccion) {
        return tipoTransaccion == TipoTransaccion.DEPOSITO_CAJERO ||
                tipoTransaccion == TipoTransaccion.DEPOSITO_OTRA_CUENTA ||
                tipoTransaccion == TipoTransaccion.DEPOSITO_SUCURSAL;
    }

    private boolean esTransaccionValidaRetiro(TipoTransaccion tipoTransaccion) {
        return tipoTransaccion == TipoTransaccion.RETIRO_CAJERO ||
                tipoTransaccion == TipoTransaccion.COMPRA_EN_LINEA ||
                tipoTransaccion == TipoTransaccion.COMPRA_FISICA;
    }
}

