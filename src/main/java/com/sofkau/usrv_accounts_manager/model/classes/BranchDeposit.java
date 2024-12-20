package com.sofkau.usrv_accounts_manager.model.classes;

import com.sofkau.usrv_accounts_manager.Utils.ConstansTrType;
import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;


@DiscriminatorValue(ConstansTrType.BRANCH_DEPOSIT)
@Document(collection = "transactions")
public class BranchDeposit extends TransactionModel {
    @Field(name = "branch_name")
    private String branchName;

    @Override
    public void processTransaction() {
        setTransactionFee(BigDecimal.valueOf(0));
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
