package org.bankAccountManager.controller;

import org.bankAccountManager.DTO.request.CustomerRequestDTO;
import org.bankAccountManager.DTO.response.CustomerResponseDTO;
import org.bankAccountManager.entity.Customer;
import org.bankAccountManager.service.implementations.CustomerServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = CustomerController.class)
class CustomerControllerTest {

    /*@Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerServiceImplementation customerService;

    private CustomerRequestDTO customerRequest;
    private CustomerResponseDTO customerResponse;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRequest = new CustomerRequestDTO();
        customerRequest.setId(1);
        customerRequest.setFirstName("John");
        customerRequest.setLastName("Doe");
        customerRequest.setEmail("john.doe@example.com");

        customerResponse = new CustomerResponseDTO();
        customerResponse.setId(1);
        customerResponse.setFirstName("John");
        customerResponse.setLastName("Doe");
        customerResponse.setEmail("john.doe@example.com");

        customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
    }

    @Test
    void createCustomer_success() {
        Mockito.when(customerService.createCustomer(any(Customer.class))).thenReturn(Mono.just(customer));

        webTestClient.post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerResponseDTO.class)
                .isEqualTo(customerResponse);
    }

    @Test
    void getCustomerById_success() {
        Mockito.when(customerService.getCustomerById(eq(1))).thenReturn(Mono.just(customer));

        webTestClient.get()
                .uri("/customers/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isFound()
                .expectBody(CustomerResponseDTO.class)
                .isEqualTo(customerResponse);
    }

    @Test
    void getCustomerById_notFound() {
        Mockito.when(customerService.getCustomerById(eq(1))).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/customers/id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllCustomers_success() {
        Mockito.when(customerService.getAllCustomers()).thenReturn(Flux.just(customer));

        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponseDTO.class)
                .hasSize(1)
                .contains(customerResponse);
    }

    @Test
    void updateCustomer_success() {
        Mockito.when(customerService.updateCustomer(any(Customer.class))).thenReturn(Mono.just(customer));

        webTestClient.put()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponseDTO.class)
                .isEqualTo(customerResponse);
    }

    @Test
    void deleteCustomer_success() {
        Mockito.when(customerService.deleteCustomer(eq(1))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteCustomer_notFound() {
        Mockito.when(customerService.deleteCustomer(eq(1))).thenReturn(Mono.error(new RuntimeException("Customer not found")));

        webTestClient.delete()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isNotFound();
    }*/
}
