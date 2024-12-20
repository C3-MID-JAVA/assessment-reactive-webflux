package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.TypeTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeTransactionRepository extends ReactiveMongoRepository<TypeTransaction, String> {

}