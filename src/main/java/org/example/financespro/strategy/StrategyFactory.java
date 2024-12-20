package org.example.financespro.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.financespro.exception.CustomException;
import org.example.financespro.model.TransactionType;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

  private final Map<TransactionType, TransactionCostStrategy> strategies;

  public StrategyFactory(List<TransactionCostStrategy> strategyList) {
    this.strategies = strategyList.stream()
            .collect(Collectors.toMap(TransactionCostStrategy::getType, Function.identity()));
  }

  public TransactionCostStrategy getStrategy(TransactionType type) {
    return Optional.ofNullable(strategies.get(type))
            .orElseThrow(() -> new CustomException("Unsupported transaction type: " + type));
  }
}
