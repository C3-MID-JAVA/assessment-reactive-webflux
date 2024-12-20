package com.sofkau.usrv_accounts_manager.model.classes;


import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "transactions")
@DiscriminatorValue(ConstansTrType.WEB_PURCHASE)
public class PaymentWebTransaction extends TransactionModel {

    @Field(name = "website")
    private String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public void processTransaction() {
        setTransactionFee(BigDecimal.valueOf(0));
    }
}
