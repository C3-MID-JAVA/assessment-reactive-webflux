package co.com.sofka.cuentabancaria.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Document(collection = "cuentas")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {
    @Id
    private String id;

    private String numeroCuenta;

    private BigDecimal saldo;

    private String titular;

    private List<Transaccion> transacciones= new ArrayList<>();

    public Cuenta(String id, String numeroCuenta, BigDecimal saldo, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.titular = titular;
        this.id = id;
    }
    public Cuenta(String numeroCuenta, BigDecimal saldo, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.titular = titular;
    }
}
