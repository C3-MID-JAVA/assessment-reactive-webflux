package com.bankmanagement.bankmanagement.service;

import com.bankmanagement.bankmanagement.dto.AccountRequestDTO;
import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponseDTO create(AccountRequestDTO accountRequestDTO);
    List<AccountResponseDTO> getAllByUserId(String userId);
    AccountResponseDTO findByAccountNumber(String accountNumber);
}
