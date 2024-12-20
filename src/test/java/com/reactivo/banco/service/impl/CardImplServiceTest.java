package com.reactivo.banco.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.model.entity.Client;
import com.reactivo.banco.repository.ClientRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.test.StepVerifier;

import java.time.LocalDate;


public class CardImplServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientImplService clientService;

    private ClientInDTO clientInDTO;
    private Client client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientInDTO = new ClientInDTO("12345", "John", "Doe", "john.doe@example.com", "123-456-7890", "123 Main St", LocalDate.of(1990, 1, 1));
        client = new Client("1", "12345", "John", "Doe", "john.doe@example.com", "123-456-7890", "123 Main St", LocalDate.of(1990, 1, 1));
    }


    @Test
    public void testSaveClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));

        StepVerifier.create(clientService.saveClient(clientInDTO))
                .expectNextMatches(savedClient -> {
                    assertEquals("1", savedClient.getId());
                    assertEquals(client.getFirstName(), savedClient.getFirstName());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testGetAllClients() {
        when(clientRepository.findAll()).thenReturn(Flux.just(client));

        StepVerifier.create(clientService.getAll())
                .expectNextMatches(clientOutDTO -> {
                    assertEquals("1", clientOutDTO.getId());
                    assertEquals(client.getFirstName(), clientOutDTO.getFirstName());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllClientsNoClients() {
        when(clientRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(clientService.getAll())
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testGetClientById() {
        when(clientRepository.findById("1")).thenReturn(Mono.just(client));

        StepVerifier.create(clientService.getById("1"))
                .expectNextMatches(clientOutDTO -> {
                    assertEquals("1", clientOutDTO.getId());
                    assertEquals(client.getFirstName(), clientOutDTO.getFirstName());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository, times(1)).findById("1");
    }

    @Test
    public void testGetClientById_NotFound() {
        when(clientRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(clientService.getById("1"))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository, times(1)).findById("1");
    }

    @Test
    public void testUpdateClient() {
        when(clientRepository.findById("1")).thenReturn(Mono.just(client));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));

        StepVerifier.create(clientService.updateClient("1", clientInDTO))
                .expectNextMatches(updatedClient -> {
                    assertEquals("1", updatedClient.getId());
                    assertEquals(client.getFirstName(), updatedClient.getFirstName());
                    return true;
                })
                .verifyComplete();

        verify(clientRepository, times(1)).findById("1");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testUpdateClientNotFound() {
        when(clientRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(clientService.updateClient("1", clientInDTO))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository, times(1)).findById("1");
    }

    @Test
    public void testDeleteClient() {
        when(clientRepository.existsById("1")).thenReturn(Mono.just(true));
        when(clientRepository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(clientService.deleteClient("1"))
                .verifyComplete();

        verify(clientRepository, times(1)).existsById("1");
        verify(clientRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteClientNotFound() {
        when(clientRepository.existsById("1")).thenReturn(Mono.just(false));

        StepVerifier.create(clientService.deleteClient("1"))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(clientRepository, times(1)).existsById("1");
    }
}
