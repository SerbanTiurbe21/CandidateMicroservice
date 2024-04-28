package com.example.candidate.repository;

import com.example.candidate.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepository extends MongoRepository<Position, String> {
    boolean existsByName(String name);
}
