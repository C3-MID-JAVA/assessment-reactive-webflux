package org.bankAccountManager.DTO.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDTO {
    private int id;
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{10,20}$", message = "Account number must be alphanumeric and between 10 and 20 characters")
    private String accountNumber;
    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(savings|current|fixed|credit)$", message = "Account type must be one of the following: savings, current, fixed, credit")
    private String accountType;
    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must not be negative")
    private BigDecimal balance;
    @NotNull(message = "Card is required")
    private List<CardRequestDTO> cards;
    @NotNull(message = "Transaction is required")
    private List<TransactionRequestDTO> transactions;
}
