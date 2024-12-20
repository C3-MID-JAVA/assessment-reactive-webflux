package ec.com.example.bank_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    @Field(name = "account_number")
    private String accountNumber;

    @Field(name = "type_transaction_identify")
    private String typeTransactionIdentify;

    @Field(name = "details")
    private String details;

    @Field(name = "value")
    private BigDecimal value;

    @Field(name = "date")
    private Date date;

    @Field(name = "status")
    private String status;

    private Account account;

    private TypeTransaction typeTransaction;

}
