package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CustomerRepositoryTest {

    /*@Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

    @BeforeEach
    public void setUp() {
        // Limpia la base de datos antes de cada prueba
        customerRepository.deleteAll();

        // Inicializa los clientes de prueba
        customer1 = new Customer();
        customer1.setId(1);
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPhone("123-456-7890");
        customer1.setAddress("123 Main St, Anytown, USA");

        customer2 = new Customer();
        customer2.setId(2);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setPhone("987-654-3210");
        customer2.setAddress("456 Elm St, Othertown, USA");

        customer3 = new Customer();
        customer3.setId(3);
        customer3.setFirstName("Alice");
        customer3.setLastName("Johnson");
        customer3.setEmail("alice.johnson@example.com");
        customer3.setPhone("555-555-5555");
        customer3.setAddress("789 Oak St, Sometown, USA");

        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
    }

    @Test
    public void testFindCustomerById_Positive() {
        // Prueba positiva: Encontrar un cliente por su ID
        Customer foundCustomer = customerRepository.findCustomerById(1);
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindCustomerById_Negative() {
        // Prueba negativa: Buscar un cliente inexistente
        Customer foundCustomer = customerRepository.findCustomerById(999);
        assertThat(foundCustomer).isNull();
    }

    @Test
    public void testExistsById_Positive() {
        // Prueba positiva: Verificar si existe un cliente con ID 1
        Boolean exists = customerRepository.existsById(1);
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsById_Negative() {
        // Prueba negativa: Verificar si existe un cliente con un ID inexistente
        Boolean exists = customerRepository.existsById(999);
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindCustomerByFirstName_Positive() {
        // Prueba positiva: Encontrar un cliente por su nombre
        Customer foundCustomer = customerRepository.findCustomerByFirstName("Jane");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    public void testFindCustomerByFirstName_Negative() {
        // Prueba negativa: Buscar un cliente con un nombre inexistente
        Customer foundCustomer = customerRepository.findCustomerByFirstName("Michael");
        assertThat(foundCustomer).isNull();
    }

    @Test
    public void testFindCustomerByLastName_Positive() {
        // Prueba positiva: Encontrar un cliente por su apellido
        Customer foundCustomer = customerRepository.findCustomerByLastName("Johnson");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("Alice");
    }

    @Test
    public void testFindCustomerByLastName_Negative() {
        // Prueba negativa: Buscar un cliente con un apellido inexistente
        Customer foundCustomer = customerRepository.findCustomerByLastName("Brown");
        assertThat(foundCustomer).isNull();
    }

    @Test
    public void testFindCustomerByEmail_Positive() {
        // Prueba positiva: Encontrar un cliente por su email
        Customer foundCustomer = customerRepository.findCustomerByEmail("john.doe@example.com");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindCustomerByEmail_Negative() {
        // Prueba negativa: Buscar un cliente con un email inexistente
        Customer foundCustomer = customerRepository.findCustomerByEmail("nonexistent@example.com");
        assertThat(foundCustomer).isNull();
    }

    @Test
    public void testFindAll_Positive() {
        // Prueba positiva: Obtener todos los clientes
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).isNotEmpty();
        assertThat(customers.size()).isEqualTo(3);
    }

    @Test
    public void testFindAll_Negative() {
        // Prueba negativa: Verificar cuando no hay clientes
        customerRepository.deleteAll();
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).isEmpty();
    }

    @Test
    public void testSaveCustomer_Positive() {
        // Prueba positiva: Guardar un nuevo cliente
        Customer newCustomer = new Customer();
        newCustomer.setId(4);
        newCustomer.setFirstName("Bob");
        newCustomer.setLastName("Williams");
        newCustomer.setEmail("bob.williams@example.com");
        newCustomer.setPhone("444-444-4444");
        newCustomer.setAddress("123 Pine St, Anothertown, USA");

        Customer savedCustomer = customerRepository.save(newCustomer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isEqualTo(4);
        assertThat(savedCustomer.getFirstName()).isEqualTo("Bob");
    }

    @Test
    public void testSaveCustomer_Negative() {
        // Prueba negativa: Intentar guardar un cliente con campos faltantes
        Customer invalidCustomer = new Customer();
        invalidCustomer.setId(5);
        invalidCustomer.setFirstName(null); // Campo faltante

        Customer savedCustomer = customerRepository.save(invalidCustomer);
        assertThat(savedCustomer.getFirstName()).isNull(); // Verifica que el campo faltante sea nulo
    }*/
}
