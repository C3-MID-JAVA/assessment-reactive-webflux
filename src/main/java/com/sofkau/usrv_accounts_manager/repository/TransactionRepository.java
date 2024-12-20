package com.sofkau.usrv_accounts_manager.repository;


import com.sofkau.usrv_accounts_manager.model.abstracts.TransactionModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<TransactionModel, String> {

}
