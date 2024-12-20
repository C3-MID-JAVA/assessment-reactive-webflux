package org.bankAccountManager.service;

import org.bankAccountManager.entity.Customer;
import org.bankAccountManager.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    /*@Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImplementation customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer(1, "John", "Doe", "john.doe@example.com", "123-456-7890", "123 Main St");
    }

    @Test
    void testCreateCustomer_success() {
        // Arrange
        when(customerRepository.existsById(customer.getId())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals(customer.getId(), createdCustomer.getId());
        assertEquals(customer.getEmail(), createdCustomer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testCreateCustomer_customerExists_throwsException() {
        // Arrange
        when(customerRepository.existsById(customer.getId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(customer);
        });

        assertEquals("Customer already exists", exception.getMessage());
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testGetCustomerById_success() {
        // Arrange
        when(customerRepository.findCustomerById(customer.getId())).thenReturn(customer);

        // Act
        Customer foundCustomer = customerService.getCustomerById(customer.getId());

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
        verify(customerRepository, times(1)).findCustomerById(customer.getId());
    }

    @Test
    void testGetCustomerById_notFound() {
        // Arrange
        when(customerRepository.findCustomerById(customer.getId())).thenReturn(null);

        // Act
        Customer foundCustomer = customerService.getCustomerById(customer.getId());

        // Assert
        assertNull(foundCustomer);
        verify(customerRepository, times(1)).findCustomerById(customer.getId());
    }

    @Test
    void testGetCustomerByFirstName_success() {
        // Arrange
        when(customerRepository.findCustomerByFirstName(customer.getFirstName())).thenReturn(customer);

        // Act
        Customer foundCustomer = customerService.getCustomerByFirstName(customer.getFirstName());

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer.getFirstName(), foundCustomer.getFirstName());
        verify(customerRepository, times(1)).findCustomerByFirstName(customer.getFirstName());
    }

    @Test
    void testGetCustomerByLastName_success() {
        // Arrange
        when(customerRepository.findCustomerByFirstName(customer.getLastName())).thenReturn(customer);

        // Act
        Customer foundCustomer = customerService.getCustomerByLastName(customer.getLastName());

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer.getLastName(), foundCustomer.getLastName());
        verify(customerRepository, times(1)).findCustomerByFirstName(customer.getLastName());
    }

    @Test
    void testGetCustomerByEmail_success() {
        // Arrange
        when(customerRepository.findCustomerByEmail(customer.getEmail())).thenReturn(customer);

        // Act
        Customer foundCustomer = customerService.getCustomerByEmail(customer.getEmail());

        // Assert
        assertNotNull(foundCustomer);
        assertEquals(customer.getEmail(), foundCustomer.getEmail());
        verify(customerRepository, times(1)).findCustomerByEmail(customer.getEmail());
    }

    @Test
    void testGetAllCustomers_success() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        // Act
        List<Customer> customers = customerService.getAllCustomers();

        // Assert
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer updatedCustomer = customerService.updateCustomer(customer);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(customer.getId(), updatedCustomer.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteCustomer() {
        // Act
        customerService.deleteCustomer(customer);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }*/
}
