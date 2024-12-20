package org.bankAccountManager.service.interfaces;

import org.bankAccountManager.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<Customer> createCustomer(Mono<Customer> customer);

    Mono<Customer> getCustomerById(Mono<Integer> id);

    Mono<Customer> getCustomerByFirstName(Mono<String> first_name);

    Mono<Customer> getCustomerByLastName(Mono<String> last_name);

    Mono<Customer> getCustomerByEmail(Mono<String> email);

    Flux<Customer> getAllCustomers();

    Mono<Customer> updateCustomer(Mono<Customer> customer);

    Mono<Void> deleteCustomer(Mono<Integer> id);
}