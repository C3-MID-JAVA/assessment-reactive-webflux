package com.bankmanagement.bankmanagement.repository;

import com.bankmanagement.bankmanagement.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeAll
    void setup() {
        user1 = new User();
        user1.setId("1");
        user1.setName("John Doe");
        user1.setDocumentId("123456789");

        user2 = new User();
        user2.setId("2");
        user2.setName("Jane Smith");
        user2.setDocumentId("987654321");
    }

    @BeforeEach
    void init() {
        userRepository.deleteAll().block();
        userRepository.saveAll(Flux.just(user1, user2)).blockLast();
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        StepVerifier.create(userRepository.findById("1"))
                .expectNextMatches(user -> user.getName().equals("John Doe") && user.getDocumentId().equals("123456789"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenUserDoesNotExist() {
        StepVerifier.create(userRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void findByDocumentId_shouldReturnUser_whenDocumentIdExists() {
        StepVerifier.create(userRepository.findByDocumentId("987654321"))
                .expectNextMatches(user -> user.getName().equals("Jane Smith"))
                .verifyComplete();
    }

    @Test
    void findByDocumentId_shouldReturnEmpty_whenDocumentIdDoesNotExist() {
        StepVerifier.create(userRepository.findByDocumentId("000000000"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistUser() {
        User newUser = new User();
        newUser.setId("3");
        newUser.setName("Alice Brown");
        newUser.setDocumentId("555555555");

        StepVerifier.create(userRepository.save(newUser))
                .expectNextMatches(user -> user.getId().equals("3") && user.getName().equals("Alice Brown"))
                .verifyComplete();

        StepVerifier.create(userRepository.findById("3"))
                .expectNextMatches(user -> user.getDocumentId().equals("555555555"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveUser() {
        StepVerifier.create(userRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(userRepository.findById("1"))
                .verifyComplete();
    }
}
