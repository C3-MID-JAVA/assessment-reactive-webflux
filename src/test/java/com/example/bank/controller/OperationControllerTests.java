package com.example.bank.controller;

import com.example.bank.dto.OperationRequestDTO;
import com.example.bank.dto.OperationResponseDTO;
import com.example.bank.service.OperationService;
import com.example.bank.utils.operations.OperationTypesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class OperationControllerTests {
    @Mock
    private OperationService operationService;

    @InjectMocks
    OperationController operationController;

    private OperationRequestDTO operationRequest;
    private OperationResponseDTO operationResponse;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(operationController).build();
        operationRequest = new OperationRequestDTO(BigDecimal.valueOf(100), OperationTypesEnum.ACCOUNT_DEPOSIT, "123");
        operationResponse = new OperationResponseDTO("123", BigDecimal.valueOf(100), OperationTypesEnum.ACCOUNT_DEPOSIT, "123");
    }

    @Test
    void createOperation_ReturnsCreated() throws Exception {
        when(operationService.createOperation(any(OperationRequestDTO.class))).thenReturn(Mono.just(operationResponse));

        webTestClient.post()
                .uri("/operations")
                .bodyValue(operationRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123");

        verify(operationService, times(1)).createOperation(any(OperationRequestDTO.class));
    }

    @Test
    void createOperation_ReturnsBadRequest() throws Exception {
        /*when(operationService.createOperation(any(OperationRequestDTO.class)))
                .thenThrow();

        webTestClient.post()
                .uri("/operations")
                .bodyValue(operationRequest)
                .exchange()
                .expectStatus().isBadRequest();

        verify(operationService, times(1)).createOperation(any(OperationRequestDTO.class));*/
    }

    @Test
    void getAllOperations_ReturnsOperationsList() throws Exception {
        List<OperationResponseDTO> operationList = List.of(operationResponse);

        when(operationService.getAllOperations()).thenReturn(Flux.fromIterable(operationList));

        webTestClient.get()
                .uri("/operations")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("123");

        verify(operationService, times(1)).getAllOperations();
    }

    @Test
    void getAllOperations_ReturnsOperationsEmpty() throws Exception {
        when(operationService.getAllOperations()).thenReturn(Flux.fromIterable(new ArrayList<>()));

        webTestClient.get()
                .uri("/operations")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");

        verify(operationService, times(1)).getAllOperations();
    }

    @Test
    void getById_ReturnsOperation() throws Exception {
        when(operationService.getOperationById("123")).thenReturn(Mono.just(operationResponse));

        webTestClient.get()
                .uri("/operations/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123");

        verify(operationService, times(1)).getOperationById("123");
    }

    @Test
    void getById_ReturnsEmpty() throws Exception {
        when(operationService.getOperationById("123")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/operations/123")
                .exchange()
                .expectStatus().isOk().expectBody().isEmpty();

        verify(operationService, times(1)).getOperationById("123");
    }
}
