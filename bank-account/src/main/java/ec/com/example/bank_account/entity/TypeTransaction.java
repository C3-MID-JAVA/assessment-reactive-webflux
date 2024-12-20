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
@Document(collection = "types_transaction")
public class TypeTransaction {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "type")
    private String type;

    @Field(name = "description")
    private String description;

    @Field(name = "value")
    private BigDecimal value;

    @Field(name = "transaction_cost")
    private Boolean transactionCost;

    @Field(name = "discount")
    private Boolean discount;

    @Field(name = "status")
    private String status;

    @DBRef
    private List<Transaction> transactions;

}