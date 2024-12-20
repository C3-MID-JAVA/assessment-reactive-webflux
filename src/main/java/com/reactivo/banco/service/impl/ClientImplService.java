package com.reactivo.banco.service.impl;


import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.mapper.ClientMapper;
import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.model.entity.Client;
import com.reactivo.banco.repository.ClientRepository;
import com.reactivo.banco.service.ClientService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ClientImplService implements ClientService {

    private final ClientRepository clientRepository;

    public ClientImplService(ClientRepository clienteRepository) {
        this.clientRepository = clienteRepository;
    }

    @Override
    public Mono<ClientOutDTO> saveClient(ClientInDTO clientInDTO) {
        Client client = ClientMapper.toEntity(clientInDTO);
        return clientRepository.save(client)
                .map(ClientMapper::toDTO);
    }

    @Override
    public Flux<ClientOutDTO> getAll() {
        return clientRepository.findAll()
                .map(ClientMapper::toDTO)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException("No existen clientes registrados.")));
    }

    @Override
    public Mono<ClientOutDTO> getById(String id) {
        return clientRepository.findById(id)
                .map(ClientMapper::toDTO)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id)));
    }

    @Override
    public Mono<ClientOutDTO> updateClient(String id, ClientInDTO clientInDTO) {
        return clientRepository.findById(id)
                .flatMap(client -> {
                    Client updatedClient = ClientMapper.toEntity(clientInDTO);
                    updatedClient.setId(client.getId());
                    updatedClient.setIdentification(client.getIdentification());
                    return clientRepository.save(updatedClient);
                })
                .map(ClientMapper::toDTO)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id)));
    }

    @Override
    public Mono<Void> deleteClient(String id) {
        return clientRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
                    }
                    return clientRepository.deleteById(id);
                });
    }


}
