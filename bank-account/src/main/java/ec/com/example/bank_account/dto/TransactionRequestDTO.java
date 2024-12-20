package ec.com.example.bank_account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "Object representing the data to create a transaction.")
@Getter
@Setter
@AllArgsConstructor
public class TransactionRequestDTO implements Serializable {

    @Schema(description = "Value of transaction", example = "100")
    @NotNull(message = "value cannot be null")
    private BigDecimal value;

    @Schema(description = "Date of transaction", example = "14-12-2024")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @Schema(description = "Account number of transaction", example = "2200000000")
    @NotNull(message = "accountNumber cannot be null")
    @Size(min = 10, max = 10, message = "Incorrect accountNumber length")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect accountNumber format")
    private String accountNumber;

    @Schema(description = "Details of transaction", example = "Transaction made in Manabí.")
    @NotNull(message = "details cannot be null")
    @Size(max = 150, message = "details exceeds allowed length")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ .,;]+$", message = "Incorrect details format")
    private String details;

    @Schema(description = "Status of transaction", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

    @Schema(description = "Transaction type of transaction")
    @NotNull(message = "typeTransactionId cannot be null")
    private String typeTransactionId;

}