package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testSaveAndFindById() {
        Client client = new Client();
        client.setIdentification("ID123456");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("123456789");
        client.setAddress("123 Main St");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        Mono<Client> saveMono = clientRepository.save(client);

        StepVerifier.create(saveMono)
                .assertNext(savedClient -> {
                    assertThat(savedClient.getId()).isNotNull(); // ID generado por MongoDB
                    assertThat(savedClient.getIdentification()).isEqualTo("ID123456");
                    assertThat(savedClient.getFirstName()).isEqualTo("John");
                    assertThat(savedClient.getLastName()).isEqualTo("Doe");
                    assertThat(savedClient.getEmail()).isEqualTo("john.doe@example.com");
                    assertThat(savedClient.getPhone()).isEqualTo("123456789");
                    assertThat(savedClient.getAddress()).isEqualTo("123 Main St");
                    assertThat(savedClient.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
                })
                .verifyComplete();

        Mono<Client> findMono = clientRepository.findById(client.getId());

        StepVerifier.create(findMono)
                .assertNext(foundClient -> {
                    assertThat(foundClient.getId()).isEqualTo(client.getId());
                    assertThat(foundClient.getIdentification()).isEqualTo("ID123456");
                    assertThat(foundClient.getFirstName()).isEqualTo("John");
                    assertThat(foundClient.getLastName()).isEqualTo("Doe");
                    assertThat(foundClient.getEmail()).isEqualTo("john.doe@example.com");
                    assertThat(foundClient.getPhone()).isEqualTo("123456789");
                    assertThat(foundClient.getAddress()).isEqualTo("123 Main St");
                    assertThat(foundClient.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
                })
                .verifyComplete();
    }

    @Test
    public void testDelete() {
        Client client = new Client();
        client.setIdentification("ID123456");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("123456789");
        client.setAddress("123 Main St");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        Mono<Void> deleteMono = clientRepository.save(client)
                .flatMap(savedClient -> clientRepository.deleteById(savedClient.getId()));

        StepVerifier.create(deleteMono)
                .verifyComplete();

        StepVerifier.create(clientRepository.findById(client.getId()))
                .expectNextCount(0) // No debe haber ningún cliente con ese ID
                .verifyComplete();
    }

}
