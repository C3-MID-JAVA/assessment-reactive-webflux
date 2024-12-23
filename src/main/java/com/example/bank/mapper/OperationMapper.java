package com.example.bank.mapper;

import com.example.bank.dto.OperationRequestDTO;
import com.example.bank.dto.OperationResponseDTO;
import com.example.bank.entity.Account;
import com.example.bank.entity.Operation;

public class OperationMapper {
    public static OperationResponseDTO toDTO(Operation op) {
        OperationResponseDTO dto = new OperationResponseDTO();

        dto.setId(op.getId());
        dto.setType(op.getType());
        dto.setValue(op.getValue());
        dto.setAccountId(op.getAccount().getId());

        return dto;
    }

    public static Operation toEntity(OperationRequestDTO dto) {
        Operation op = new Operation();

        op.setId(dto.getId());
        op.setType(dto.getType());
        op.setValue(dto.getValue());
        op.setAccount(new Account(dto.getAccountId()));

        return op;
    }

    public static Operation toEntity(OperationRequestDTO dto, Account acc) {
        Operation op = new Operation();

        op.setId(dto.getId());
        op.setType(dto.getType());
        op.setValue(dto.getValue());
        op.setAccount(acc);

        return op;
    }
}