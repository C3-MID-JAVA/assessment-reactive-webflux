package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Customer;
import org.bankAccountManager.repository.CustomerRepository;
import org.bankAccountManager.service.interfaces.CustomerService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomerServiceImplementation(CustomerRepository customerRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.customerRepository = customerRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Customer> createCustomer(Mono<Customer> customer) {
        return customer.flatMap(cEnt ->
                customerRepository.existsById(cEnt.getId()).flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException("Account already exists"));
                    return reactiveMongoTemplate.save(cEnt);
                })
        );
    }

    @Override
    public Mono<Customer> getCustomerById(Mono<Integer> id) {
        return id.flatMap(customerRepository::findCustomerById);
    }

    @Override
    public Mono<Customer> getCustomerByFirstName(Mono<String> first_name) {
        return first_name.flatMap(customerRepository::findCustomerByFirstName);
    }

    @Override
    public Mono<Customer> getCustomerByLastName(Mono<String> last_name) {
        return last_name.flatMap(customerRepository::findCustomerByLastName);
    }

    @Override
    public Mono<Customer> getCustomerByEmail(Mono<String> email) {
        return email.flatMap(customerRepository::findCustomerByEmail);
    }

    @Override
    public Flux<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<Customer> updateCustomer(Mono<Customer> customer) {
        return customer.flatMap(cEnt ->
                reactiveMongoTemplate.findAndModify(
                                Query.query(Criteria.where("id").is(cEnt.getId())),
                                new Update()
                                        .set("firstName", cEnt.getFirstName())
                                        .set("lastName", cEnt.getLastName())
                                        .set("email", cEnt.getEmail())
                                        .set("phone", cEnt.getPhone())
                                        .set("address", cEnt.getAddress()),
                                Customer.class)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found"))));
    }

    @Override
    public Mono<Void> deleteCustomer(Mono<Integer> id) {
        return id.flatMap(customerRepository::deleteById);
    }
}
