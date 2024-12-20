package org.bankAccountManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Transaction")
public class Transaction {
    @Id
    private int id;
    @DBRef
    private Branch branch;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String description;
    @JsonIgnore
    @DBRef
    private Account destinationAccount;
    @JsonIgnore
    @DBRef
    private Account sourceAccount;
}
