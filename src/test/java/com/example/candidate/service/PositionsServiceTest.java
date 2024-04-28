package com.example.candidate.service;

import com.example.candidate.exception.PositionAlreadyExistsException;
import com.example.candidate.exception.PositionNotFoundException;
import com.example.candidate.model.Position;
import com.example.candidate.repository.PositionsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionsServiceTest {
    @Mock
    private PositionsRepository positionsRepository;
    @InjectMocks
    private PositionsServiceImpl positionsService;
    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position("1", "Project Manager");
    }

    @AfterEach
    void tearDown() {
        position = null;
    }

    @Test
    void shouldGetAllPositions(){
        when(positionsRepository.findAll()).thenReturn(Collections.singletonList(position));

        List<Position> positions = positionsService.getAllPositions();
        assertFalse(positions.isEmpty());
        assertNotNull(positions);

        verify(positionsRepository).findAll();
    }

    @Test
    void shouldGetPositionById() {
        when(positionsRepository.findById(position.getId())).thenReturn(java.util.Optional.of(position));

        Position positionById = positionsService.getPositionById(position.getId());

        assertNotNull(positionById);
        verify(positionsRepository).findById(position.getId());
    }

    @Test
    void getPositionByIdShouldThrowExceptionWhenPositionNotFound(){
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.getPositionById(position.getId()));
    }

    @Test
    void shouldAddPosition(){
        when(positionsRepository.existsByName(position.getName())).thenReturn(false);
        when(positionsRepository.save(position)).thenReturn(position);

        Position addedPosition = positionsService.addPosition(position);
        assertNotNull(addedPosition);

        verify(positionsRepository).save(position);
    }

    @Test
    void addPositionShouldThrowExceptionWhenPositionAlreadyExists(){
        when(positionsRepository.existsByName(position.getName())).thenReturn(true);

        assertThrows(PositionAlreadyExistsException.class, () -> positionsService.addPosition(position));
    }

    @Test
    void shouldUpdatePosition(){
        Position updatedPosition = new Position("1", "Software Developer");
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.of(position));
        when(positionsRepository.save(position)).thenReturn(updatedPosition);

        Position positionToUpdate = positionsService.updatePosition(position.getId(), updatedPosition);
        assertNotNull(positionToUpdate);

        verify(positionsRepository).save(position);
    }

    @Test
    void updatePositionShouldThrowExceptionWhenPositionNotFound(){
        Position updatedPosition = new Position("1", "Software Developer");
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.updatePosition(position.getId(), updatedPosition));
    }

    @Test
    void shouldDeletePosition(){
        positionsService.deletePosition(position.getId());
        verify(positionsRepository).deleteById(position.getId());
    }
}
