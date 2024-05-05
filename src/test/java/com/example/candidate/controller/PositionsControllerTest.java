package com.example.candidate.controller;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import com.example.candidate.service.PositionsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionsControllerTest {
    @Mock
    private PositionsService positionsService;
    @InjectMocks
    private PositionsController positionsController;
    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position("1", "Project Manager", Status.OPEN, null);
    }

    @AfterEach
    void tearDown() {
        position = null;
    }

    @Test
    void shouldGetAllPositions() {
        when(positionsService.getAllPositions()).thenReturn(List.of(position));

        ResponseEntity<List<Position>> response = positionsController.getAllPositions().block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(position), response.getBody());
        verify(positionsService).getAllPositions();
    }

    @Test
    void shouldGetPositionById() {
        when(positionsService.getPositionById("1")).thenReturn(position);

        ResponseEntity<Position> response = positionsController.getPositionById("1").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(position, response.getBody());
        verify(positionsService).getPositionById("1");
    }

    @Test
    void shouldAddPosition() {
        when(positionsService.addPosition(any(Position.class))).thenReturn(position);

        ResponseEntity<Position> response = positionsController.addPosition(position).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(position, response.getBody());
        verify(positionsService).addPosition(any(Position.class));
    }

    @Test
    void shouldUpdatePosition() {
        when(positionsService.updatePosition("1", position)).thenReturn(position);

        ResponseEntity<Position> response = positionsController.updatePosition("1", position).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(position, response.getBody());
        verify(positionsService).updatePosition("1", position);
    }

    @Test
    void shouldDeactivatePosition() {
        ResponseEntity<Void> response = positionsController.deactivatePosition("1", SubStatus.FILLED).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(positionsService).deactivatePosition("1", SubStatus.FILLED);
    }

    @Test
    void shouldGetPositionsByStatus() {
        when(positionsService.getPositionsByStatus(Status.OPEN)).thenReturn(List.of(position));

        ResponseEntity<List<Position>> response = positionsController.getPositionsByStatus(Status.OPEN).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(position), response.getBody());
        verify(positionsService).getPositionsByStatus(Status.OPEN);
    }

    @Test
    void shouldGetPositionsByStatusAndSubStatus() {
        when(positionsService.getPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.FILLED)).thenReturn(List.of(position));

        ResponseEntity<List<Position>> response = positionsController.getPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.FILLED).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(position), response.getBody());
        verify(positionsService).getPositionsByStatusAndSubStatus(Status.OPEN, SubStatus.FILLED);
    }

}
