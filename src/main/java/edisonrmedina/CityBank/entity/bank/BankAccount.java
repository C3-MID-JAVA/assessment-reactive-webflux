package edisonrmedina.CityBank.entity.bank;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "bank_accounts")
@Data // Genera getters, setters, equals, hashCode, toString
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder // Habilita el patrón Builder
public class BankAccount {

    @Id
    private String id; // Cambiado a String porque MongoDB usa ObjectId como String

    private String accountHolder;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    // Constructor, getters, setters, etc.

    public BankAccount(String accountHolder, BigDecimal balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

}
