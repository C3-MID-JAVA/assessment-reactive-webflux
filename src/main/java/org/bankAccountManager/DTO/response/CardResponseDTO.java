package org.bankAccountManager.DTO.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponseDTO {
    private int id;
    private String cardNumber;
    private String cardType;
    private LocalDateTime expirationDate;
    private String cvv;
}
