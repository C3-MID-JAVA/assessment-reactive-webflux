package org.example.financespro.mapper;

import org.example.financespro.dto.response.AccountResponseDto;
import org.example.financespro.model.Account;

public class AccountMapper {

  private AccountMapper() {}

  public static AccountResponseDto toResponseDTO(Account account) {
    return new AccountResponseDto(
        account.getId(), account.getAccountNumber(), account.getBalance().doubleValue());
  }
}
