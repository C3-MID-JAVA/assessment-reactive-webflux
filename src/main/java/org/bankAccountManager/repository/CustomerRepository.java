package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, Long> {
    Mono<Customer> findCustomerById(int id);

    Mono<Boolean> existsById(int id);

    Mono<Customer> findCustomerByFirstName(String first_name);

    Mono<Customer> findCustomerByLastName(String last_name);

    Mono<Customer> findCustomerByEmail(String email);

    Flux<Customer> findAll();

    Mono<Void> deleteById(int id);
}
