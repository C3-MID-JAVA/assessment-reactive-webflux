package ec.com.example.bank_account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Schema(description = "Object representing the data to create a account type.")
@Getter
@Setter
@AllArgsConstructor
public class TypeAccountRequestDTO implements Serializable {

    @Schema(description = "Type of account type", example = "Debit account.")
    @NotNull(message = "type cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect type format")
    private String type;

    @Schema(description = "Description of account type", example = "User debit account.")
    @NotNull(message = "description cannot be null")
    @Size(max = 100, message = "description exceeds allowed length")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ .,;]+$", message = "Incorrect description format")
    private String description;

    @Schema(description = "Status of account type", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

}