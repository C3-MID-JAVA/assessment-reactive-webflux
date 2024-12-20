package edisonrmedina.CityBank.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountDTO {
    @NotBlank(message = "El nombre del titular de la cuenta no puede estar vacío")
    @Size(max = 100, message = "El nombre del titular no puede superar los 100 caracteres")
    private String accountHolder;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial debe ser mayor o igual a 0")
    @Digits(integer = 12, fraction = 2, message = "El saldo inicial debe tener como máximo 12 dígitos enteros y 2 decimales")
    private BigDecimal balance;
}
