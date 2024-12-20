package com.reactivo.banco.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.mapper.ClientMapper;
import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.model.entity.Client;
import com.reactivo.banco.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;


public class ClientImplServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientImplService clientImplService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientImplService = new ClientImplService(clientRepository);
    }

    @Test
    public void testSaveClient() {
        ClientInDTO clientInDTO = new ClientInDTO("123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        Client client_ = new Client("123", "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        ClientOutDTO clientOutDTO = new ClientOutDTO("123", "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));

        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client_));

        StepVerifier.create(clientImplService.saveClient(clientInDTO))
                .expectNextMatches(client -> {
                    assertEquals("123", client.getId());
                    assertEquals("123", client.getIdentification());
                    assertEquals("John", client.getFirstName());
                    assertEquals("Doe", client.getLastName());
                    assertEquals("john@example.com", client.getEmail());
                    assertEquals("123456789", client.getPhone());
                    assertEquals("1234 Elm Street", client.getAddress());
                    assertEquals(LocalDate.of(1985, 5, 15), client.getBirthDate());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void testGetAllClients() {
        Client client1 = new Client("123", "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        Client client2 = new Client("124", "124", "Jane", "Doe", "jane@example.com", "987654321", "5678 Oak Street", LocalDate.of(1990, 10, 25));
        ClientOutDTO expectedClientOutDTO1 = new ClientOutDTO("123", "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        ClientOutDTO expectedClientOutDTO2 = new ClientOutDTO("124", "124", "Jane", "Doe", "jane@example.com", "987654321", "5678 Oak Street", LocalDate.of(1990, 10, 25));

        when(clientRepository.findAll()).thenReturn(Flux.just(client1, client2));

        StepVerifier.create(clientImplService.getAll())
                .expectNextMatches(clientOutDTO -> {
                    assertEquals(expectedClientOutDTO1.getId(), clientOutDTO.getId());
                    assertEquals(expectedClientOutDTO1.getIdentification(), clientOutDTO.getIdentification());
                    assertEquals(expectedClientOutDTO1.getFirstName(), clientOutDTO.getFirstName());
                    assertEquals(expectedClientOutDTO1.getLastName(), clientOutDTO.getLastName());
                    assertEquals(expectedClientOutDTO1.getEmail(), clientOutDTO.getEmail());
                    assertEquals(expectedClientOutDTO1.getPhone(), clientOutDTO.getPhone());
                    assertEquals(expectedClientOutDTO1.getAddress(), clientOutDTO.getAddress());
                    assertEquals(expectedClientOutDTO1.getBirthDate(), clientOutDTO.getBirthDate());
                    return true;
                })
                .expectNextMatches(clientOutDTO -> {
                    assertEquals(expectedClientOutDTO2.getId(), clientOutDTO.getId());
                    assertEquals(expectedClientOutDTO2.getIdentification(), clientOutDTO.getIdentification());
                    assertEquals(expectedClientOutDTO2.getFirstName(), clientOutDTO.getFirstName());
                    assertEquals(expectedClientOutDTO2.getLastName(), clientOutDTO.getLastName());
                    assertEquals(expectedClientOutDTO2.getEmail(), clientOutDTO.getEmail());
                    assertEquals(expectedClientOutDTO2.getPhone(), clientOutDTO.getPhone());
                    assertEquals(expectedClientOutDTO2.getAddress(), clientOutDTO.getAddress());
                    assertEquals(expectedClientOutDTO2.getBirthDate(), clientOutDTO.getBirthDate());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository).findAll();
    }


    @Test
    public void testGetClientById() {
        String id = "123";
        Client client = new Client(id, "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        ClientOutDTO expectedClientOutDTO = new ClientOutDTO(id, "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));

        when(clientRepository.findById(id)).thenReturn(Mono.just(client));

        StepVerifier.create(clientImplService.getById(id))
                .expectNextMatches(clientOutDTO -> {
                    assertEquals(expectedClientOutDTO.getId(), clientOutDTO.getId());
                    assertEquals(expectedClientOutDTO.getIdentification(), clientOutDTO.getIdentification());
                    assertEquals(expectedClientOutDTO.getFirstName(), clientOutDTO.getFirstName());
                    assertEquals(expectedClientOutDTO.getLastName(), clientOutDTO.getLastName());
                    assertEquals(expectedClientOutDTO.getEmail(), clientOutDTO.getEmail());
                    assertEquals(expectedClientOutDTO.getPhone(), clientOutDTO.getPhone());
                    assertEquals(expectedClientOutDTO.getAddress(), clientOutDTO.getAddress());
                    assertEquals(expectedClientOutDTO.getBirthDate(), clientOutDTO.getBirthDate());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository).findById(id);
    }


    @Test
    public void testGetClientByIdNotFound() {
        String id = "123";

        when(clientRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(clientImplService.getById(id))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository).findById(id);
    }

    @Test
    public void testUpdateClient() {
        String id = "123";
        ClientInDTO clientInDTO = new ClientInDTO("123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        Client existingClient = new Client(id, "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        Client updatedClient = new Client(id, "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));
        ClientOutDTO expectedClientOutDTO = new ClientOutDTO(id, "123", "John", "Doe", "john@example.com", "123456789", "1234 Elm Street", LocalDate.of(1985, 5, 15));

        when(clientRepository.findById(id)).thenReturn(Mono.just(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(updatedClient));

        StepVerifier.create(clientImplService.updateClient(id, clientInDTO))
                .expectNextMatches(clientOutDTO -> {
                    assertEquals(expectedClientOutDTO.getId(), clientOutDTO.getId());
                    assertEquals(expectedClientOutDTO.getIdentification(), clientOutDTO.getIdentification());
                    assertEquals(expectedClientOutDTO.getFirstName(), clientOutDTO.getFirstName());
                    assertEquals(expectedClientOutDTO.getLastName(), clientOutDTO.getLastName());
                    assertEquals(expectedClientOutDTO.getEmail(), clientOutDTO.getEmail());
                    assertEquals(expectedClientOutDTO.getPhone(), clientOutDTO.getPhone());
                    assertEquals(expectedClientOutDTO.getAddress(), clientOutDTO.getAddress());
                    assertEquals(expectedClientOutDTO.getBirthDate(), clientOutDTO.getBirthDate());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository).findById(id);
        verify(clientRepository).save(any(Client.class));
    }


    @Test
    public void testDeleteClient() {
        String id = "123";

        when(clientRepository.existsById(id)).thenReturn(Mono.just(true));
        when(clientRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(clientImplService.deleteClient(id))
                .verifyComplete();

        verify(clientRepository).existsById(id);
        verify(clientRepository).deleteById(id);
    }

    @Test
    public void testDeleteClientNotFound() {
        String id = "123";

        when(clientRepository.existsById(id)).thenReturn(Mono.just(false));

        StepVerifier.create(clientImplService.deleteClient(id))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository).existsById(id);
    }

}
