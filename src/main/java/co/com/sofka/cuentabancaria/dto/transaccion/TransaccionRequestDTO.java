package co.com.sofka.cuentabancaria.dto.transaccion;

import co.com.sofka.cuentabancaria.model.enums.TipoTransaccion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionRequestDTO {

    @NotNull
    private String numeroCuenta;

    @NotNull
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull
    private TipoTransaccion tipoTransaccion;

}
