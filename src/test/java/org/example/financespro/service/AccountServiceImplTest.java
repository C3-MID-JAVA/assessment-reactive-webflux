package org.example.financespro.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.example.financespro.dto.request.AccountRequestDto;
import org.example.financespro.dto.response.AccountResponseDto;
import org.example.financespro.model.Account;
import org.example.financespro.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AccountServiceImplTest {

  @Mock private AccountRepository accountRepository;

  @InjectMocks private AccountServiceImpl accountService;

  public AccountServiceImplTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateAccount_Success() {
    AccountRequestDto request = new AccountRequestDto("12345", BigDecimal.valueOf(1000));
    Account account = Account.create(null, "12345", BigDecimal.valueOf(1000));

    when(accountRepository.save(any())).thenReturn(Mono.just(account));

    Mono<AccountResponseDto> result = accountService.createAccount(request);

    StepVerifier.create(result)
        .expectNextMatches(response -> response.getAccountNumber().equals("12345"))
        .verifyComplete();
  }
}
