package ec.com.example.bank_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(collection = "cards")
public class Card {

    @Id
    private String id;

    @Field(name = "holder_name")
    private String holderName;

    @Field(name = "limitation")
    private BigDecimal limitation;

    @Field(name = "cvc_code")
    private String cvcCode;

    @Field(name = "expiration_date")
    private Date expirationDate;

    @Field(name = "status")
    private String status;

    @DBRef
    private Account account;

}