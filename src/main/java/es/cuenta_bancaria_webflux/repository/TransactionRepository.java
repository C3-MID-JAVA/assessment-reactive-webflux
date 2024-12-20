package es.cuenta_bancaria_webflux.repository;
import es.cuenta_bancaria_webflux.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {
    Flux<Transaction> findByIdCuenta(String idCuenta);
}
