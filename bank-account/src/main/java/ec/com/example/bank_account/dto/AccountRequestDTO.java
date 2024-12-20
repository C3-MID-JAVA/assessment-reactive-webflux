package ec.com.example.bank_account.dto;

import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.entity.TypeAccount;
import ec.com.example.bank_account.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Object representing the data to create a account.")
@Getter
@Setter
@AllArgsConstructor
public class AccountRequestDTO implements Serializable {

    @Schema(description = "Number of account", example = "2200000000")
    @NotNull(message = "number cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect number format")
    @Size(min = 10, max = 10, message = "Incorrect number length")
    private String number;

    @Schema(description = "Available balance of account", example = "200")
    @NotNull(message = "availableBalance cannot be null")
    private BigDecimal availableBalance;

    @Schema(description = "Retained balance of account", example = "10")
    @NotNull(message = "retainedBalance cannot be null")
    private BigDecimal retainedBalance;

    @Schema(description = "Status of account", example = "ACTIVE")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

    @Schema(description = "Account User ID")
    @NotNull(message = "userId cannot be null")
    private String userId;

    @Schema(description = "Account type of account")
    @NotNull(message = "typeAccountId cannot be null")
    private String typeAccountId;

}