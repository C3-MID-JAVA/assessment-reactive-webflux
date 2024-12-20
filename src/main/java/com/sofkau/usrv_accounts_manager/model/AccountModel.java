package com.sofkau.usrv_accounts_manager.model;

import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class AccountModel {

    @Id
    private String id;

    @Field(name = "account_number")
    private String accountNumber;

    @Field(name = "account_balance")
    private BigDecimal balance;

    @Field(name = "owner_name")
    private String ownerName;

    @Field(name = "account_type")
    private String accountType;

    @DBRef
    @Field(name = "cards")
    private List<CardModel> cards;

    @DBRef
    @Field(name = "transactions")
    private List<TransactionModel> transactions;



}
