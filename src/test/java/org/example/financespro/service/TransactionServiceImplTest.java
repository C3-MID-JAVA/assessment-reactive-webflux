package org.example.financespro.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.example.financespro.dto.request.TransactionRequestDto;
import org.example.financespro.dto.response.TransactionResponseDto;
import org.example.financespro.model.Account;
import org.example.financespro.repository.AccountRepository;
import org.example.financespro.repository.TransactionRepository;
import org.example.financespro.strategy.StrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TransactionServiceImplTest {

  @Mock private AccountRepository accountRepository;

  @Mock private TransactionRepository transactionRepository;

  @Mock private StrategyFactory strategyFactory;

  @InjectMocks private TransactionServiceImpl transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldProcessTransactionSuccessfully() {
    TransactionRequestDto request =
        new TransactionRequestDto("123", "ATM_WITHDRAWAL", BigDecimal.valueOf(500));
    Account account = Account.create("123", "123456", BigDecimal.valueOf(1000));

    when(accountRepository.findById("123")).thenReturn(Mono.just(account));
    when(transactionRepository.save(any())).thenReturn(Mono.empty());

    Mono<TransactionResponseDto> result = transactionService.processTransaction(request);

    StepVerifier.create(result)
        .expectNextMatches(response -> response.getTransactionType().equals("ATM_WITHDRAWAL"))
        .verifyComplete();
  }
}
