package com.example.candidate.repository;

import com.example.candidate.model.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
class PositionsRepositoryTest {
    @Autowired
    private PositionsRepository positionsRepository;
    private Position position, position1;

    @BeforeEach
    public void setUp() {
        position = new Position("1", "Project Manager");
        position1 = new Position("2", "Developer");
        positionsRepository.saveAll(List.of(position, position1));
    }

    @AfterEach
    public void tearDown() {
        positionsRepository.delete(position);
        positionsRepository.delete(position1);
    }

    @Test
    void shouldReturnTrueIfPositionExistsByName() {
        boolean exists = positionsRepository.existsByName("Developer");
        assertThat(exists).isTrue();
    }
}
