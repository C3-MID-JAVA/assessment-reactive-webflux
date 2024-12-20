package org.bankAccountManager.DTO.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {
    private int id;
    @NotNull(message = "Branch is required")
    private BranchRequestDTO branch;
    @NotNull(message = "Transaction date is required")
    private LocalDateTime date;
    @NotNull(message = "Transaction's type is required")
    @Pattern(regexp = "^(branch_transfer|another_account_deposit|store_card_purchase|online_card_purchase|atm_withdrawal|atm_deposit)$",
            message = "Invalid transaction type. Valid types are: branch_transfer, another_account_deposit, " +
                    "store_card_purchase, online_card_purchase, atm_withdrawal, atm_deposit")
    private String type;
    @NotNull(message = "Transaction amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    //@NotNull(message = "Destination account is required")
    private AccountRequestDTO destinationAccount;
    //@NotNull(message = "Destination account is required")
    private AccountRequestDTO sourceAccount;
}
