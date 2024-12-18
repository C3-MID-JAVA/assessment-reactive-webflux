package com.reactivo.banco.service.impl;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.CardInDTO;
import com.reactivo.banco.model.dto.CardOutDTO;
import com.reactivo.banco.repository.AccountRepository;
import com.reactivo.banco.repository.CardRepository;
import com.reactivo.banco.service.CardService;
import com.reactivo.banco.mapper.CardMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CardImplService implements CardService {

    private final CardRepository tarjetaRepository;
    private final AccountRepository cuentaRepository;

    public CardImplService(CardRepository tarjetaRepository, AccountRepository cuentaRepository) {
        this.tarjetaRepository = tarjetaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Mono<CardOutDTO> crearTarjeta(CardInDTO tarjetaInDTO) {
        return null;
    }

//    @Override
//    public Mono<CardOutDTO> crearTarjeta(CardInDTO tarjetaInDTO) {
//        return cuentaRepository.findById(tarjetaInDTO.getCuentaId())
//                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + tarjetaInDTO.getCuentaId())))
//                .flatMap(cuenta -> {
//                    Card tarjeta = CardMapper.mapToEntity(tarjetaInDTO, cuenta);
//                    return tarjetaRepository.save(tarjeta);
//                })
//                .map(CardMapper::mapToDTO);
//    }

    @Override
    public Flux<CardOutDTO> obtenerTodasLasTarjetas() {
        return tarjetaRepository.findAll()
                .map(CardMapper::toDTO)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException("No existen tarjetas registradas.")));
    }

    @Override
    public Mono<CardOutDTO> obtenerTarjetaPorId(String id) {
        return tarjetaRepository.findById(id)
                .map(CardMapper::toDTO)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Tarjeta no encontrada con ID: " + id)));
    }

    @Override
    public Mono<CardOutDTO> actualizarTarjeta(String id, CardInDTO tarjetaInDTO) {
        return null;
    }

//    @Override
//    public Mono<CardOutDTO> actualizarTarjeta(String id, CardInDTO tarjetaInDTO) {
//        return tarjetaRepository.findById(id)
//                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Tarjeta no encontrada con ID: " + id)))
//                .flatMap(tarjeta -> cuentaRepository.findById(tarjetaInDTO.getCuentaId())
//                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + tarjetaInDTO.getCuentaId())))
//                        .flatMap(cuenta -> {
//                            tarjeta.setNumeroTarjeta(tarjetaInDTO.getNumeroTarjeta());
//                            tarjeta.setTipo(tarjetaInDTO.getTipo());
//                            tarjeta.setCuenta(cuenta);
//                            return tarjetaRepository.save(tarjeta);
//                        }))
//                .map(CardMapper::mapToDTO);
//    }

    @Override
    public Mono<Void> eliminarTarjeta(String id) {
        return tarjetaRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException("Tarjeta no encontrada con ID: " + id));
                    }
                    return tarjetaRepository.deleteById(id);
                });
    }
}