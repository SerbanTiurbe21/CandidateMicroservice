package com.example.candidate.service;

import com.example.candidate.exception.*;
import com.example.candidate.model.Candidate;
import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import com.example.candidate.repository.CandidateRepository;
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
    @Mock
    private CandidateRepository candidateRepository;
    @InjectMocks
    private PositionsServiceImpl positionsService;
    private Position position;
    private Position closedPosition;
    final private String candidateId = "candidate123";

    @BeforeEach
    void setUp() {
        position = new Position("1", "Project Manager", Status.OPEN, null, null);
        closedPosition = new Position("1", "Developer", Status.CLOSED, SubStatus.CANCELLED, null);
    }

    @AfterEach
    void tearDown() {
        position = null;
        closedPosition = null;
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
    void shouldGetPositionsByName(){
        when(positionsRepository.findPositionsByName(position.getName())).thenReturn(Collections.singletonList(position));

        List<Position> positionsByName = positionsService.getPositionsByName(position.getName());
        assertFalse(positionsByName.isEmpty());
        assertNotNull(positionsByName);

        verify(positionsRepository).findPositionsByName(position.getName());
    }

    @Test
    void shouldAddPositionWhenNotExisting() {
        when(positionsRepository.findPositionsByName("Developer")).thenReturn(Collections.emptyList());
        when(positionsRepository.save(any(Position.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null, null);
        Position savedPosition = positionsService.addPosition(newPosition);

        assertNotNull(savedPosition);
        assertEquals(Status.OPEN, savedPosition.getStatus());
        assertNull(savedPosition.getSubStatus());
        verify(positionsRepository).save(newPosition);
    }

    @Test
    void shouldThrowExceptionWhenPositionExistsAndNotClosed() {
        when(positionsRepository.findPositionsByName("Developer")).thenReturn(Collections.singletonList(position));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null, null);
        assertThrows(PositionAlreadyExistsException.class, () -> positionsService.addPosition(newPosition));

        verify(positionsRepository, never()).save(newPosition);
    }

    @Test
    void shouldAddPositionWhenExistingPositionIsClosed() {
        when(positionsRepository.findPositionsByName("Developer")).thenReturn(Collections.singletonList(closedPosition));
        when(positionsRepository.save(any(Position.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Position newPosition = new Position("1", "Developer", Status.OPEN, null, null);
        Position savedPosition = positionsService.addPosition(newPosition);

        assertNotNull(savedPosition);
        assertEquals(Status.OPEN, savedPosition.getStatus());
        assertNull(savedPosition.getSubStatus());
        verify(positionsRepository).save(newPosition);
    }

    @Test
    void shouldUpdatePosition(){
        Position updatedPosition = new Position("1", "Software Developer", Status.CLOSED, SubStatus.CANCELLED, null);
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.of(position));
        when(positionsRepository.save(position)).thenReturn(updatedPosition);

        Position positionToUpdate = positionsService.updatePosition(position.getId(), updatedPosition);
        assertNotNull(positionToUpdate);

        verify(positionsRepository).save(position);
    }

    @Test
    void updatePositionShouldThrowExceptionWhenPositionNotFound(){
        Position updatedPosition = new Position("1", "Software Developer", Status.OPEN, null, null);
        when(positionsRepository.findById(position.getId())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionsService.updatePosition(position.getId(), updatedPosition));
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

    @Test
    void cancelPositionShouldThrowNotFoundException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(PositionNotFoundException.class,
                () -> positionsService.cancelPosition("1"));
    }

    @Test
    void cancelPositionShouldThrowAlreadyDeactivatedException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(closedPosition));
        assertThrows(PositionAlreadyDeactivatedException.class,
                () -> positionsService.cancelPosition("1"));
    }

    @Test
    void cancelPositionShouldThrowDeactivationNotAllowedException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(position));
        when(candidateRepository.countCandidatesByPositionId("1")).thenReturn(1);
        assertThrows(DeactivationNotAllowedException.class,
                () -> positionsService.cancelPosition("1"));
    }

    @Test
    void shouldCancelPositionSuccessfully() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(position));
        when(candidateRepository.countCandidatesByPositionId("1")).thenReturn(0);
        positionsService.cancelPosition("1");
        assertEquals(Status.CLOSED, position.getStatus());
        assertEquals(SubStatus.CANCELLED, position.getSubStatus());
        verify(positionsRepository).save(position);
    }

    @Test
    void fillPositionShouldThrowNotFoundException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(PositionNotFoundException.class,
                () -> positionsService.fillPosition("1", candidateId));
    }

    @Test
    void fillPositionShouldThrowAlreadyDeactivatedException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(closedPosition));
        assertThrows(PositionAlreadyDeactivatedException.class,
                () -> positionsService.fillPosition("1", candidateId));
    }

    @Test
    void fillPositionShouldThrowCandidateNotFoundException() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(position));
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());
        assertThrows(CandidateNotFoundException.class,
                () -> positionsService.fillPosition("1", candidateId));
    }

    @Test
    void shouldFillPositionSuccessfully() {
        when(positionsRepository.findById("1")).thenReturn(Optional.of(position));
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new Candidate(candidateId, "John Doe", "","","", null,null,null, null, false)));

        positionsService.fillPosition("1", candidateId);
        assertEquals(Status.CLOSED, position.getStatus());
        assertEquals(SubStatus.FILLED, position.getSubStatus());
        assertEquals(candidateId, position.getHiredCandidateId());
        verify(positionsRepository).save(position);
    }

    @Test
    void shouldGetPositionsByStatuses(){
        when(positionsRepository.findPositionsByStatuses(Status.OPEN, Status.CLOSED)).thenReturn(Collections.singletonList(position));

        List<Position> positionsByStatuses = positionsService.getPositionsByStatuses(Status.OPEN, Status.CLOSED);
        assertFalse(positionsByStatuses.isEmpty());
        assertNotNull(positionsByStatuses);

        verify(positionsRepository).findPositionsByStatuses(Status.OPEN, Status.CLOSED);
    }
}
