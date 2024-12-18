package com.reactivo.banco.service;

import com.reactivo.banco.model.dto.CardInDTO;
import com.reactivo.banco.model.dto.CardOutDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface CardService {

    Mono<CardOutDTO> crearTarjeta(CardInDTO tarjetaInDTO);

    Flux<CardOutDTO> obtenerTodasLasTarjetas();

    Mono<CardOutDTO> obtenerTarjetaPorId(String id);

    Mono<CardOutDTO> actualizarTarjeta(String id, CardInDTO tarjetaInDTO);

    Mono<Void> eliminarTarjeta(String id);
}
