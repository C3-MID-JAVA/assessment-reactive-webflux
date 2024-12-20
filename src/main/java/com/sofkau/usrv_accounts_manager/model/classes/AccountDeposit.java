package com.sofkau.usrv_accounts_manager.model.classes;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "transactions")
public class AccountDeposit extends TransactionModel {

    @DBRef
    @Field(name = "account_receiver")
    private AccountModel accountReceiver;


    @Override
    public void processTransaction() {
        setTransactionFee(BigDecimal.valueOf(1.5));
    }

    public AccountModel getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(AccountModel accountReceiver) {
        this.accountReceiver = accountReceiver;
    }
}
