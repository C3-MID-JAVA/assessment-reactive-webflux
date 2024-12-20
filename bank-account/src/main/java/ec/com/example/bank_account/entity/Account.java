package ec.com.example.bank_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "number")
    private String number;

    @Field(name = "available_balance")
    private BigDecimal availableBalance;

    @Field(name = "retained_balance")
    private BigDecimal retainedBalance;

    @Field(name = "status")
    private String status;

    private User user;

    private TypeAccount typeAccount;

    @DBRef
    private List<Card> cards;

    @DBRef
    private List<Transaction> transactions;

}