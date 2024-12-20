package org.bankAccountManager.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Account")
public class Account {
    @Id
    private int id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    @DBRef
    private List<Card> cards;
    @DBRef
    private List<Transaction> transactions;
}
