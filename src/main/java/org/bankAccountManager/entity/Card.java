package org.bankAccountManager.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Card")
public class Card {
    @Id
    private int id;
    private String cardNumber;
    private String cardType;
    private LocalDateTime expirationDate;
    private String cvv;
}
