package es.cuenta_bancaria_webflux.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String idTransaccion;

    @NotNull(message = "El monto de la transacción es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotBlank(message = "El tipo de transacción es obligatorio")
    @Size(max= 50)
    private String tipo;

    @DecimalMin(value = "0.0", message = "El costo de transacción no puede ser negativo")
    private BigDecimal costo;

    private String idCuenta;
}
