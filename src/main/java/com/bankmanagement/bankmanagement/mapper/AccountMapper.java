package com.bankmanagement.bankmanagement.mapper;

import com.bankmanagement.bankmanagement.dto.AccountResponseDTO;
import com.bankmanagement.bankmanagement.model.Account;

public class AccountMapper {
    public static AccountResponseDTO fromEntity(Account account){
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountNumber(account.getAccountNumber());
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setUserId(account.getUserId());
        return accountResponseDTO;
    }
}
