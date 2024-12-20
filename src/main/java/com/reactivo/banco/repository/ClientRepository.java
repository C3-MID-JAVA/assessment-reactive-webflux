package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
}
