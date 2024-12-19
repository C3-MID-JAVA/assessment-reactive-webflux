package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>{
    Mono<User> findByDocumentId(String documentId);
}
