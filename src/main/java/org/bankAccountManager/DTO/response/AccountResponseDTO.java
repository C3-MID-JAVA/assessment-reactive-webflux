package org.bankAccountManager.DTO.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {
    private int id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private List<CardResponseDTO> cards;
    private List<TransactionResponseDTO> transactions;
}
