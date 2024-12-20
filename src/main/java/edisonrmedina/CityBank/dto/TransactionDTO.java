package edisonrmedina.CityBank.dto;

import edisonrmedina.CityBank.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @NotNull(message = "El monto de la transacción no puede ser nulo")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto de la transacción debe ser mayor a 0")
    @Digits(integer = 12, fraction = 2, message = "El monto debe tener como máximo 12 dígitos enteros y 2 decimales")
    private BigDecimal amount;

    @NotNull(message = "El ID de la cuenta bancaria no puede ser nulo")
    @Positive(message = "El ID de la cuenta bancaria debe ser un número positivo")
    private String bankAccountId;

    @NotNull(message = "El costo de la transacción no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo de la transacción debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2, message = "El costo de la transacción debe tener como máximo 12 dígitos enteros y 2 decimales")
    private BigDecimal transactionCost;

    private TransactionType transactionType;

    public TransactionDTO(TransactionType type, BigDecimal amount, String id, BigDecimal transactionCost) {
    }

    public TransactionType getType() {
        return transactionType;
    }
}
