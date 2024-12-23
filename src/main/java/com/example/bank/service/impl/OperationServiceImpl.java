package com.example.bank.service.impl;

import com.example.bank.dto.OperationRequestDTO;
import com.example.bank.dto.OperationResponseDTO;
import com.example.bank.entity.Account;
import com.example.bank.entity.Operation;
import com.example.bank.mapper.OperationMapper;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.OperationRepository;
import com.example.bank.service.OperationService;
import com.example.bank.utils.operations.OperationType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;

    private final AccountRepository accountRepository;

    public OperationServiceImpl(OperationRepository operationRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<OperationResponseDTO> getAllOperations() {
        return operationRepository.findAll()
                .map(OperationMapper::toDTO)
                .switchIfEmpty(Flux.error(new RuntimeException("No operations found.")));
    }

    @Override
    public Mono<OperationResponseDTO> getOperationById(String id) {
        return operationRepository.findById(id)
                .map(OperationMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Operation not found.")));
    }

    @Override
    public Mono<OperationResponseDTO> createOperation(OperationRequestDTO operationDTO) {
        return accountRepository.getAccountById(operationDTO.getAccountId())
                .flatMap(acc -> {
                    var op = applyOperation(OperationMapper.toEntity(operationDTO, acc));
                    return operationRepository.save(op);
                })
                .map(OperationMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found.")));
    }

    private Operation applyOperation(Operation op) {
        Account account = op.getAccount();
        OperationType opType = Operation.getOperationTypeObj(op.getType());
        BigDecimal cost = opType.cost();
        BigDecimal value = op.getValue();

        BigDecimal operationTotal;
        switch (opType.action()) {
            case DEBIT -> operationTotal = value.add(cost).negate();
            case CREDIT -> operationTotal = value.subtract(cost);
            default -> operationTotal = BigDecimal.ZERO;
        }

        account.setBalance(account.getBalance().add(operationTotal));

        return op;
    }

    @Override
    public void revertOperation(int id) {}
}