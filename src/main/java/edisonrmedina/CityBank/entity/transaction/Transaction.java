package edisonrmedina.CityBank.entity.transaction;

import edisonrmedina.CityBank.entity.bank.BankAccount;
import edisonrmedina.CityBank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Document(collection = "transactions")
@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Builder // Habilita el patrón Builder
public class Transaction {

    @Id
    private String id;

    private TransactionType type; // "DEPOSIT", "WITHDRAWAL", "PURCHASE"
    private BigDecimal amount;
    private BigDecimal transactionCost;
    private LocalDateTime timestamp;

    @DBRef(lazy = false) // Referencia a otra colección
    private BankAccount bankAccount;


    // Constructor, getters, setters, etc.

    public Transaction(TransactionType type, BigDecimal amount, BigDecimal transactionCost, BankAccount bankAccount) {
        this.type = type;
        this.amount = amount;
        this.transactionCost = transactionCost;
        this.bankAccount = bankAccount;
        this.timestamp = LocalDateTime.now();
    }

    public void setBankAccount(Optional<BankAccount> account) {
        this.bankAccount = account.orElse(null);
    }

}
