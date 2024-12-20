package ec.com.example.bank_account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Object representing the data to create a transaction type.")
@Getter
@Setter
@AllArgsConstructor
public class TypeTransactionRequestDTO implements Serializable {

    @Schema(description = "Type of transaction type", example = "Deposit from branch")
    @NotNull(message = "type cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ -]+$", message = "Incorrect type format")
    private String type;

    @Schema(description = "Description of transaction type", example = "Deposits made from a branch.")
    @NotNull(message = "description cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ .,;]+$", message = "Incorrect description format")
    private String description;

    @Schema(description = "Value of transaction type", example = "1")
    @NotNull(message = "value cannot be null")
    private BigDecimal value;

    @Schema(description = "Transaction cost of transaction type", example = "true")
    @NotNull(message = "transactionCost cannot be null")
    private Boolean transactionCost;

    @Schema(description = "Discount of transaction type", example = "true")
    @NotNull(message = "discount cannot be null")
    private Boolean discount;

    @Schema(description = "Status of transaction type", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

}