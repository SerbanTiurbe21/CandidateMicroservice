package com.example.candidate.repository;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class PositionsRepositoryTest {
    @Autowired
    private PositionsRepository positionsRepository;
    private Position position, position1;

    @BeforeEach
    public void setUp() {
        position = new Position("1", "Project Manager", Status.OPEN, null, null);
        position1 = new Position("2", "Developer", Status.OPEN, null, null);
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

    @Test
    void shouldReturnPositionByName() {
        Optional<Position> optionalPosition = positionsRepository.findByName("Developer");
        assertThat(optionalPosition).isPresent();
        Position position = optionalPosition.orElseThrow(() -> new NoSuchElementException("Position not found"));
        assertThat(position).isEqualTo(position1);
    }

    @Test
    void shouldReturnListOfPositionsByStatus() {
        List<Position> positions = positionsRepository.findPositionsByStatus(Status.OPEN);
        assertTrue(positions.contains(position));
        assertTrue(positions.contains(position1));
    }

    @Test
    void shouldReturnListOfPositionsByStatusAndSubStatus() {
        List<Position> positions = positionsRepository.findPositionsByStatusAndSubStatus(Status.OPEN, null);
        assertTrue(positions.contains(position));
        assertTrue(positions.contains(position1));
    }

    @Test
    void shouldReturnListOfPositionsByName() {
        List<Position> positions = positionsRepository.findPositionsByName("Developer");
        assertTrue(positions.contains(position1));
    }
}
