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

@Schema(description = "Object representing the data to create a card.")
@Getter
@Setter
@AllArgsConstructor
public class CardRequestDTO implements Serializable {

    @Schema(description = "Holder name of card", example = "Diego Loor")
    @NotNull(message = "holderName cannot be null")
    @Size(max = 30, message = "holderName exceeds allowed length")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect holderName format")
    private String holderName;

    @Schema(description = "Limitation of card", example = "1000")
    @NotNull(message = "limitation cannot be null")
    private BigDecimal limitation;

    @Schema(description = "CVC Code of card", example = "456")
    @NotNull(message = "cvcCode cannot be null")
    @Size(min = 3, max = 3, message = "Incorrect cvcCode length")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect cvcCode format")
    private String cvcCode;

    @Schema(description = "Expiration date of card", example = "10-10-2030")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date expirationDate;

    @Schema(description = "Status of card", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

    @Schema(description = "Account of card")
    @NotNull(message = "accountId cannot be null")
    private String accountId;

}