package ec.com.example.bank_account.repository;

import ec.com.example.bank_account.entity.TypeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAccountRepository extends ReactiveMongoRepository<TypeAccount, String> {

}