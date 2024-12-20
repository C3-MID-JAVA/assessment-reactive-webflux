package co.com.sofka.cuentabancaria.repository;

import co.com.sofka.cuentabancaria.model.Cuenta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CuentaRepository  extends ReactiveMongoRepository<Cuenta, String> {
    Mono<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
