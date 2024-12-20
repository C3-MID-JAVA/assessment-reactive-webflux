package org.example.financespro.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import org.example.financespro.exception.CustomException;
import org.example.financespro.model.TRANSACTION_TYPE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class StrategyFactoryTest {

  private StrategyFactory strategyFactory;

  @BeforeEach
  void setUp() {
    List<TransactionCostStrategy> strategies =
        List.of(new ATMDepositStrategy(), new DepositStrategy(), new OnlinePurchaseStrategy());
    strategyFactory = new StrategyFactory(strategies);
  }

  @Test
  void shouldReturnCorrectStrategy() {
    Mono<TransactionCostStrategy> strategyMono =
        Mono.just(strategyFactory.getStrategy(TRANSACTION_TYPE.ATM_DEPOSIT));

    strategyMono
        .map(
            strategy -> {
              assertNotNull(strategy);
              assertEquals(BigDecimal.valueOf(2.00), strategy.calculateCost(BigDecimal.TEN));
              return strategy;
            })
        .block(); // `block` usado aquí solo para simplificar el test.
  }

  @Test
  void shouldThrowExceptionForUnsupportedType() {
    Mono<TransactionCostStrategy> strategyMono =
        Mono.just(strategyFactory.getStrategy(TRANSACTION_TYPE.PHYSICAL_PURCHASE));

    CustomException exception = assertThrows(CustomException.class, () -> strategyMono.block());
    assertEquals("Unsupported transaction type: PHYSICAL_PURCHASE", exception.getMessage());
  }
}
