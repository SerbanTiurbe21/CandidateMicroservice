package com.example.candidate.service;

import com.example.candidate.exception.PositionAlreadyExistsException;
import com.example.candidate.exception.PositionNotFoundException;
import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PositionsServiceTest {
    @Mock
    private PositionsRepository positionsRepository;
    @InjectMocks
    private PositionsServiceImpl positionsService;
    private Position position;
    private Position closedPosition;

    @BeforeEach
    void setUp() {
        position = new Position("1", "Project Manager", Status.OPEN, null);
        closedPosition = new Position("1", "Developer", Status.CLOSED, null);
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
    void shouldGetPositionByName(){
        when(positionsRepository.findByName(position.getName())).thenReturn(Optional.of(position));

        Position positionByName = positionsService.getPositionByName(position.getName());
        assertNotNull(positionByName);

        verify(positionsRepository).findByName(position.getName());
    }

    @Test
    void getPositionByNameShouldThrowExceptionWhenPositionNotFound(){
        when(positionsRepository.findByName(position.getName())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.getPositionByName(position.getName()));
    }

    @Test
    void shouldAddPositionWhenNotExisting() {
        when(positionsRepository.findByName("Developer")).thenReturn(Optional.empty());
        when(positionsRepository.save(any(Position.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null);
        Position savedPosition = positionsService.addPosition(newPosition);

        assertNotNull(savedPosition);
        assertEquals(Status.OPEN, savedPosition.getStatus());
        assertNull(savedPosition.getSubStatus());
        verify(positionsRepository).save(newPosition);
    }

    @Test
    void shouldThrowExceptionWhenPositionExistsAndNotClosed() {
        when(positionsRepository.findByName("Developer")).thenReturn(Optional.of(position));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null);

        assertThrows(PositionAlreadyExistsException.class, () -> positionsService.addPosition(newPosition));
        verify(positionsRepository, never()).save(any(Position.class));
    }

    @Test
    void shouldAddPositionWhenExistingPositionIsClosed() {
        when(positionsRepository.findByName("Developer")).thenReturn(Optional.of(closedPosition));
        when(positionsRepository.save(any(Position.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null);
        Position savedPosition = positionsService.addPosition(newPosition);

        assertNotNull(savedPosition);
        assertEquals(Status.OPEN, savedPosition.getStatus());
        assertNull(savedPosition.getSubStatus());
        verify(positionsRepository).save(newPosition);
    }

    @Test
    void shouldUpdatePosition(){
        Position updatedPosition = new Position("1", "Software Developer", Status.CLOSED, SubStatus.CANCELLED);
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.of(position));
        when(positionsRepository.save(position)).thenReturn(updatedPosition);

        Position positionToUpdate = positionsService.updatePosition(position.getId(), updatedPosition);
        assertNotNull(positionToUpdate);

        verify(positionsRepository).save(position);
    }

    @Test
    void updatePositionShouldThrowExceptionWhenPositionNotFound(){
        Position updatedPosition = new Position("1", "Software Developer", Status.OPEN, null);
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.updatePosition(position.getId(), updatedPosition));
    }

    @Test
    void shouldDeactivatePosition(){
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.of(position));

        positionsService.deactivatePosition(position.getId(), SubStatus.CANCELLED);
        verify(positionsRepository).save(position);
    }

    @Test
    void deactivatePositionShouldThrowExceptionWhenPositionNotFound(){
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.deactivatePosition(position.getId(), SubStatus.CANCELLED));
    }

    @Test
    void shouldGetPositionsByStatus(){
        when(positionsRepository.findPositionsByStatus(Status.OPEN)).thenReturn(Collections.singletonList(position));

        List<Position> positionsByStatus = positionsService.getPositionsByStatus(Status.OPEN);
        assertFalse(positionsByStatus.isEmpty());
        assertNotNull(positionsByStatus);

        verify(positionsRepository).findPositionsByStatus(Status.OPEN);
    }

    @Test
    void shouldGetPositionsByStatusAndSubStatus(){
        when(positionsRepository.findPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.CANCELLED)).thenReturn(Collections.singletonList(position));

        List<Position> positionsByStatusAndSubStatus = positionsService.getPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.CANCELLED);
        assertFalse(positionsByStatusAndSubStatus.isEmpty());
        assertNotNull(positionsByStatusAndSubStatus);

        verify(positionsRepository).findPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.CANCELLED);
    }
}
