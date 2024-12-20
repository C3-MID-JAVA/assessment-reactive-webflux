package co.com.sofka.cuentabancaria.dto.cuenta;

import co.com.sofka.cuentabancaria.model.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaResponseDTO {

    private String id;

    private String numeroCuenta;

    private BigDecimal saldo;

    private String titular;

    public CuentaResponseDTO(Cuenta cuenta) {
        this.id = cuenta.getId();
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.saldo = cuenta.getSaldo();
        this.titular = cuenta.getTitular();
    }

}
