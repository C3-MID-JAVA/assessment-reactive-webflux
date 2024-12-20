package co.com.sofka.cuentabancaria.repository;

import co.com.sofka.cuentabancaria.dto.transaccion.TransaccionResponseDTO;
import co.com.sofka.cuentabancaria.model.Transaccion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransaccionRepository extends ReactiveMongoRepository<Transaccion, String> {

    Flux<Transaccion> findByCuentaId(String cuentaId);
}
