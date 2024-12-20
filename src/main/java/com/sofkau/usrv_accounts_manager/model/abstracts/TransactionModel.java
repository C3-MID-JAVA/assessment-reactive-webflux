package com.sofkau.usrv_accounts_manager.model.abstracts;


import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public abstract class TransactionModel {

    @Id
    private String id;

    @Field(name = "description")
    private String description;

    @Field(name = "ammount")
    private BigDecimal amount;

    @Field(name = "transaction_type_showed")
    private String transactionType;

    @Field(name = "transaction_fee")
    private BigDecimal transactionFee;

    @Field(name = "created_at")
    private LocalDateTime timestamp;


    @DBRef
    @Field(name = "account")
    private AccountModel account;

    @DBRef
    @Field(name = "card")
    private CardModel card;

    public abstract void processTransaction();

}
