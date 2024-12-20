package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends ReactiveMongoRepository<Branch, Long> {
    Mono<Branch> findBranchById(int id);

    Mono<Boolean> existsById(int id);

    Mono<Branch> findBranchByName(String name);

    Flux<Branch> findAll();

    Mono<Void> deleteById(int id);
}
