package co.com.sofka.cuentabancaria.dto.transaccion;

import co.com.sofka.cuentabancaria.model.Transaccion;
import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO {

    private String id;

    private String numeroCuenta;

    private TipoTransaccion tipoTransaccion;

    private BigDecimal monto;

    private BigDecimal costoTransaccion;

    private BigDecimal nuevoSaldo;

    private LocalDateTime fecha;

    public TransaccionResponseDTO(Transaccion transaccion, BigDecimal saldo, String numeroCuenta) {
        this.id = transaccion.getId();
        this.tipoTransaccion = transaccion.getTipo();
        this.monto = transaccion.getMonto();
        this.costoTransaccion = transaccion.getCostoTransaccion();
        this.fecha = transaccion.getFecha();
        this.nuevoSaldo = saldo;
        this.numeroCuenta = numeroCuenta;
    }

    public TransaccionResponseDTO(Transaccion transaccion) {
    }
}
