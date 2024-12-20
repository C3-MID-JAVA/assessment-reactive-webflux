package co.com.sofka.bankingaccount.bankingaccount.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    @NotBlank
    @NotNull
    private String id;
    @NotNull
    @NotBlank
    private String type;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal cost;
}
