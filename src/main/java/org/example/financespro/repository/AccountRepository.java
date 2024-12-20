package org.example.financespro.repository;

import org.example.financespro.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
  Mono<Account> findByAccountNumber(String accountNumber);
}
