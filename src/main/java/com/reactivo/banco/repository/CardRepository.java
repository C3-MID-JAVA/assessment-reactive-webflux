package com.reactivo.banco.repository;

import com.reactivo.banco.model.entity.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, String> {
}
