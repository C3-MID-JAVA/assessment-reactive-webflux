package org.bankAccountManager.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
    private int id;
    private BranchResponseDTO branch;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String description;
    @JsonIgnore
    private AccountResponseDTO destinationAccount;
    @JsonIgnore
    private AccountResponseDTO sourceAccount;
}
