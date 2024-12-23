package com.example.bank.service;

import com.example.bank.dto.OperationRequestDTO;
import com.example.bank.dto.OperationResponseDTO;
import com.example.bank.entity.Operation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OperationService {
    Flux<OperationResponseDTO> getAllOperations();
    Mono<OperationResponseDTO> getOperationById(String id);
    Mono<OperationResponseDTO> createOperation(OperationRequestDTO operationDTO);
    private Operation applyOperation(Operation operation) { return operation; };
    void revertOperation(int id);
}