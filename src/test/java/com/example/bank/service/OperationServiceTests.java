package com.example.bank.service;

import com.example.bank.dto.OperationRequestDTO;
import com.example.bank.dto.OperationResponseDTO;
import com.example.bank.entity.Account;
import com.example.bank.entity.Operation;
import com.example.bank.mapper.OperationMapper;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.OperationRepository;
import com.example.bank.service.impl.OperationServiceImpl;
import com.example.bank.utils.operations.OperationTypesEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTests {
    @Mock
    OperationRepository operationRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    OperationServiceImpl operationService;

    @Test
    void operationCreatedOK() {
        String accountId = "36f53041-";
        Account account = new Account(accountId, "olivia", BigDecimal.valueOf(1000), new ArrayList<>());

        OperationRequestDTO newOperation = new OperationRequestDTO(
                BigDecimal.valueOf(100),
                OperationTypesEnum.ACCOUNT_DEPOSIT,
                accountId
        );

        String operationId = newOperation.getId();

        when(accountRepository.getAccountById(accountId)).thenReturn(Mono.just(account));
        when(operationRepository.save(any(Operation.class))).thenReturn(Mono.just(OperationMapper.toEntity(newOperation, account)));

        Mono<OperationResponseDTO> response = operationService.createOperation(newOperation);

        assertNotNull(response);
        assertEquals(operationId, response.block().getId());
    }

    @Test
    void getAllOperationsOK() {
        Account account = new Account("123", "holder1", BigDecimal.valueOf(100), new ArrayList<>());
        List<Operation> operations = List.of(
                new Operation("1", BigDecimal.valueOf(100), OperationTypesEnum.ACCOUNT_DEPOSIT, account),
                new Operation("2", BigDecimal.valueOf(200), OperationTypesEnum.ACCOUNT_DEPOSIT, account)
        );

        when(operationRepository.findAll()).thenReturn(Flux.fromIterable(operations));

        var response = operationService.getAllOperations();

        assertNotNull(response);
        assertEquals(2, response.count().block());
        assertEquals("1", response.elementAt(0).block().getId());
        assertEquals("2", response.elementAt(1).block().getId());
    }

    @Test
    void getAllOperationsFail() {
        List<Operation> operations = new ArrayList<>();

        when(operationRepository.findAll()).thenReturn(Flux.fromIterable(operations));

        var response = operationService.getAllOperations();

        StepVerifier.create(response).expectErrorMessage("No operations found.").verify();
    }

    @Test
    void getOperationByIdOK() {
        Account account = new Account("123", "holder1", BigDecimal.valueOf(100), new ArrayList<>());
        String operationId = "1";
        Operation operation = new Operation(operationId, BigDecimal.valueOf(100), OperationTypesEnum.ACCOUNT_DEPOSIT, account);

        when(operationRepository.findById(operationId)).thenReturn(Mono.just(operation));

        var response = operationService.getOperationById(operationId);

        assertNotNull(response);
        assertEquals(operationId, response.block().getId());
    }

    @Test
    void getOperationByIdFail() {
        String accountId = "2";

        when(operationRepository.findById(accountId)).thenReturn(Mono.empty());

        var response = operationService.getOperationById(accountId);

        StepVerifier.create(response).expectErrorMessage("Operation not found.").verify();
    }
}