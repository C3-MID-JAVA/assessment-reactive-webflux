package com.reactivo.banco.service;

import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface ClientService {


    Mono<ClientOutDTO> saveClient(ClientInDTO clientInDto);

    Flux<ClientOutDTO> getAll();

    Mono<ClientOutDTO> getById(String id);

    Mono<ClientOutDTO> updateClient(String id, ClientInDTO clientInDto);

    Mono<Void> deleteClient(String id);
}
