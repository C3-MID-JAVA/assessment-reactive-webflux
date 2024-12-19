package co.com.sofka.cuentabancaria.service;

import co.com.sofka.cuentabancaria.config.exceptions.ConflictException;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaRequestDTO;
import co.com.sofka.cuentabancaria.dto.cuenta.CuentaResponseDTO;
import co.com.sofka.cuentabancaria.model.Cuenta;
import co.com.sofka.cuentabancaria.repository.CuentaRepository;
import co.com.sofka.cuentabancaria.service.iservice.CuentaService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class CuentaServiceImpl  implements CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Flux<CuentaResponseDTO> obtenerCuentas() {
        return cuentaRepository.findAll()
                .map(CuentaResponseDTO::new);
    }

    @Override
    public Mono<CuentaResponseDTO> crearCuenta(CuentaRequestDTO cuentaRequestDTO) {
        return cuentaRepository.findByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())
                .flatMap(existingCuenta ->
                        Mono.<Cuenta>error(new ConflictException("El número de cuenta ya está registrado.")))
                .switchIfEmpty(Mono.defer(() -> {
                    Cuenta nuevaCuenta = new Cuenta();
                    nuevaCuenta.setNumeroCuenta(cuentaRequestDTO.getNumeroCuenta());
                    nuevaCuenta.setSaldo(cuentaRequestDTO.getSaldoInicial());
                    nuevaCuenta.setTitular(cuentaRequestDTO.getTitular());

                    return cuentaRepository.save(nuevaCuenta); // Devuelve Mono<Cuenta>
                }))
                .map(CuentaResponseDTO::new); // Mapea Cuenta a CuentaResponseDTO
    }

    @Override
    public Mono<CuentaResponseDTO> obtenerCuentaPorId(String id) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No se encontro el cuenta con id: " + id)))
                .map(CuentaResponseDTO::new);
    }

    @Override
    public Mono<BigDecimal> consultarSaldo(String id) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No se encontro el cuenta con id: " + id)))
                .map(Cuenta::getSaldo);
    }
}
