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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cards")
public class CardModel {
    @Id
    private String id;

    @Field(name = "card_name")
    private String cardName;

    @Field(name = "card_number")
    private String cardNumber;

    @Field(name = "card_type")
    private String cardType;

    @Field(name = "card_status")
    private String cardStatus;

    @Field(name = "card_expiry_date")
    private String cardExpiryDate;

    @Field(name = "card_cvv")
    private String cardCVV;

    @Field(name = "card_limit")
    private BigDecimal cardLimit;

    @Field(name = "card_holder_name")
    private String cardHolderName;

    @DBRef
    @Field(name = "account")
    private  AccountModel account;

    @DBRef
    @Field(name = "transactions")
    private List<TransactionModel> transactions;

}
