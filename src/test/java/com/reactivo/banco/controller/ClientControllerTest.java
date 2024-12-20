package com.reactivo.banco.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reactivo.banco.exception.ResourceNotFoundException;
import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveClientSuccessfully() {
        ClientInDTO clientInDTO = new ClientInDTO("1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));
        ClientOutDTO clientOutDTO = new ClientOutDTO("1", "1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));

        when(clientService.saveClient(any(ClientInDTO.class))).thenReturn(Mono.just(clientOutDTO));

        ResponseEntity<Mono<ClientOutDTO>> response = clientController.saveClient(clientInDTO);

        StepVerifier.create(response.getBody())
                .expectSubscription()
                .expectNextMatches(client -> {
                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertEquals("1", client.getId());
                    assertEquals("John", client.getFirstName());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldGetAllClientsSuccessfully() {
        ClientOutDTO clientOutDTO = new ClientOutDTO("1", "1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));

        when(clientService.getAll()).thenReturn(Flux.just(clientOutDTO));

        ResponseEntity<Flux<ClientOutDTO>> response = clientController.getAll();

        StepVerifier.create(response.getBody())
                .expectSubscription()
                .expectNextMatches(client -> {
                    assertEquals("1", client.getId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldGetClientByIdSuccessfully() {
        String clientId = "1";
        ClientOutDTO clientOutDTO = new ClientOutDTO(clientId, "1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));

        when(clientService.getById(clientId)).thenReturn(Mono.just(clientOutDTO));

        ResponseEntity<Mono<ClientOutDTO>> response = clientController.getById(clientId);

        StepVerifier.create(response.getBody())
                .expectSubscription()
                .expectNextMatches(client -> {
                    assertEquals(clientId, client.getId());
                    assertEquals("John", client.getFirstName());
                    return true;
                })
                .verifyComplete();
    }



    @Test
    void shouldUpdateClientSuccessfully() {
        String clientId = "1";
        ClientInDTO clientInDTO = new ClientInDTO("1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));
        ClientOutDTO updatedClientOutDTO = new ClientOutDTO(clientId, "1234567890", "John", "Doe", "john.doe@example.com", "+573001234567", "Main Street 123", LocalDate.of(1990, 5, 20));

        when(clientService.updateClient(eq(clientId), any(ClientInDTO.class))).thenReturn(Mono.just(updatedClientOutDTO));

        ResponseEntity<Mono<ClientOutDTO>> response = clientController.updateClient(clientId, clientInDTO);

        StepVerifier.create(response.getBody())
                .expectSubscription()
                .expectNextMatches(client -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(clientId, client.getId());
                    assertEquals("John", client.getFirstName());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    void shouldDeleteClientSuccessfully() {
        String clientId = "1";

        when(clientService.deleteClient(clientId)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> responseMono = clientController.deleteClient(clientId);

        StepVerifier.create(responseMono)
                .expectSubscription()
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
                    return true;
                })
                .verifyComplete();
    }


}
