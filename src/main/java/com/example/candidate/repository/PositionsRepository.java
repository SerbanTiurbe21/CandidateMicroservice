package com.example.candidate.repository;

import com.example.candidate.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PositionsRepository extends MongoRepository<Position, String> {
    boolean existsByName(String name);
    Optional<Position> findByName(String name);
}