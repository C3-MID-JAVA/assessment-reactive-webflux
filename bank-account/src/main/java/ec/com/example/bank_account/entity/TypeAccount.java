package ec.com.example.bank_account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "types_account")
public class TypeAccount {

    @Id
    private String id;

    @Field(name = "type")
    private String type;

    @Field(name = "description")
    private String description;

    @Field(name = "status")
    private String status;

    @DBRef
    private List<Account> accounts;

}