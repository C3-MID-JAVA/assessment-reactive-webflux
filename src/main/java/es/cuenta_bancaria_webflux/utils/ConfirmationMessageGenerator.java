package es.cuenta_bancaria_webflux.utils;

import es.cuenta_bancaria_webflux.model.Transaction;

import java.util.function.Supplier;

public class ConfirmationMessageGenerator {
    public static Supplier<String> createConfirmationMessage( Transaction transaction){
        return ()-> String.format("Transacción realizada con éxito. ID Cuenta: %s, Monto: %s, Tipo: %s, Costo: %s",
                transaction.getIdCuenta(), transaction.getMonto(), transaction.getTipo(), transaction.getCosto());

    }
}
