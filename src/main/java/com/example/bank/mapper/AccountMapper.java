package com.example.bank.mapper;

import com.example.bank.dto.AccountRequestDTO;
import com.example.bank.dto.AccountResponseDTO;
import com.example.bank.entity.Account;

import java.util.ArrayList;

public class AccountMapper {
    public static AccountResponseDTO toDTO(Account acc) {
        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setId(acc.getId());
        dto.setHolder(acc.getHolder());
        dto.setBalance(acc.getBalance());
        dto.setOperations(new ArrayList<>());

        return dto;
    }

    public static Account toEntity(AccountRequestDTO dto) {
        Account acc = new Account();

        acc.setId(dto.getId());
        acc.setHolder(dto.getHolder());
        acc.setBalance(dto.getBalance());
        acc.setOperations(new ArrayList<>());

        return acc;
    }
}