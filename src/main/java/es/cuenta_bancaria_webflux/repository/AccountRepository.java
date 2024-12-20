package es.cuenta_bancaria_webflux.repository;


import es.cuenta_bancaria_webflux.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account,String> {
}
