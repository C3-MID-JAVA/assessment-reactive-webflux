package com.example.bank.repository;

import com.example.bank.entity.Operation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends ReactiveMongoRepository<Operation, String> {
}