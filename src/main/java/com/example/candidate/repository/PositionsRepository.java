package com.example.candidate.repository;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionsRepository extends MongoRepository<Position, String> {
    boolean existsByName(String name);
    Optional<Position> findByName(String name);
    List<Position> findPositionsByStatus(Status status);
}
