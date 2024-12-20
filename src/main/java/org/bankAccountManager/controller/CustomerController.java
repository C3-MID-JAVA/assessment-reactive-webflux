package org.bankAccountManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bankAccountManager.DTO.request.CustomerRequestDTO;
import org.bankAccountManager.DTO.response.CustomerResponseDTO;
import org.bankAccountManager.entity.Customer;
import org.bankAccountManager.service.implementations.CustomerServiceImplementation;
import org.bankAccountManager.service.interfaces.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.bankAccountManager.mapper.DTORequestMapper.toCustomer;
import static org.bankAccountManager.mapper.DTOResponseMapper.toCustomerResponseDTO;

@Tag(name = "Customer Management", description = "Endpoints for managing customers")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerServiceImplementation customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer", description = "Add a new customer to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Mono<ResponseEntity<CustomerResponseDTO>> createCustomer(@RequestBody CustomerRequestDTO customer) {
        return customerService.createCustomer(toCustomer(Mono.just(customer)))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.CREATED).body(customerResponseDTO)));
    }

    @Operation(summary = "Retrieve a customer by ID", description = "Get details of a customer by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/id")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerById(@RequestBody CustomerRequestDTO customer) {
        return customerService.getCustomerById(toCustomer(Mono.just(customer))
                        .map(Customer::getId))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(customerResponseDTO)));
    }

    @Operation(summary = "Retrieve a customer by first name", description = "Get details of a customer by their first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/firstName")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByFirstName(@RequestBody CustomerRequestDTO customer) {
        return customerService.getCustomerByFirstName(toCustomer(Mono.just(customer))
                        .map(Customer::getFirstName))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(customerResponseDTO)));
    }

    @Operation(summary = "Retrieve a customer by last name", description = "Get details of a customer by their last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/lastName")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByLastName(@RequestBody CustomerRequestDTO customer) {
        return customerService.getCustomerByLastName(toCustomer(Mono.just(customer))
                        .map(Customer::getLastName))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(customerResponseDTO)));
    }

    @Operation(summary = "Retrieve a customer by email", description = "Get details of a customer by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/email")
    public Mono<ResponseEntity<CustomerResponseDTO>> getCustomerByEmail(@RequestBody CustomerRequestDTO customer) {
        return customerService.getCustomerByEmail(toCustomer(Mono.just(customer))
                        .map(Customer::getEmail))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.FOUND).body(customerResponseDTO)));
    }

    @Operation(summary = "Retrieve all customers", description = "Get a list of all customers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<CustomerResponseDTO>>> getAllCustomers() {
        return Mono.just(
                ResponseEntity.ok(
                        customerService.getAllCustomers()
                                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity)))
                )
        );
    }

    @Operation(summary = "Update a customer", description = "Update details of an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping
    public Mono<ResponseEntity<CustomerResponseDTO>> updateCustomer(@RequestBody CustomerRequestDTO customer) {
        return customerService.updateCustomer(toCustomer(Mono.just(customer)))
                .flatMap(customerEntity -> toCustomerResponseDTO(Mono.just(customerEntity))
                        .map(customerResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(customerResponseDTO)));
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the system by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteCustomer(@RequestBody CustomerRequestDTO customer) {
        return toCustomer(Mono.just(customer)) // Convierte el DTO a la entidad Account
                .flatMap(customerEntity ->
                        customerService.deleteCustomer(Mono.just(customerEntity.getId()))
                                .thenReturn(ResponseEntity.noContent().<Void>build())
                                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()))
                );
    }
}
