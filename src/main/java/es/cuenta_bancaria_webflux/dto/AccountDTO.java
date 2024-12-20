package es.cuenta_bancaria_webflux.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String idCuenta;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 100, message = "El nombre del titular no debe exceder 100 caracteres")
    private String titular;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0",  message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    private List<String> transacciones; // Lista de IDs de las transacciones asociadas
}
