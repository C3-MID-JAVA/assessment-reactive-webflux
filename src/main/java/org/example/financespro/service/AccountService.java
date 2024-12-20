package org.example.financespro.service;

import org.example.financespro.dto.request.AccountRequestDto;
import org.example.financespro.dto.response.AccountResponseDto;
import reactor.core.publisher.Mono;

public interface AccountService {

  /**
   * Creates a new account reactively.
   *
   * @param requestDTO the account creation request details
   * @return a Mono containing the created account details
   */
  Mono<AccountResponseDto> createAccount(AccountRequestDto requestDTO);

  /**
   * Retrieves an account reactively by its unique account number.
   *
   * @param accountNumber the unique account number
   * @return a Mono containing the account details
   */
  Mono<AccountResponseDto> getAccountDetailsByNumber(String accountNumber);
}
