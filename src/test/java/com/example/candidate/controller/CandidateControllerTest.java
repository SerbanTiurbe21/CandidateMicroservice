package com.example.candidate.controller;

import com.example.candidate.model.Candidate;
import com.example.candidate.service.CandidateService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {
    @Mock
    private CandidateService candidateService;
    @InjectMocks
    private CandidatesController candidatesController;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = new Candidate("1", "John Doe", "1234567890", "http://example.com/cv", "john.doe@example.com", null, null, null, null, false);
    }

    @AfterEach
    public void tearDown() {
        candidate = null;
    }

    @Test
    void shouldAddCandidate() {
        when(candidateService.addCandidate(any(Candidate.class))).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidatesController.addCandidate(candidate).block();

        assert response != null;
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).addCandidate(any(Candidate.class));
    }

    @Test
    void shouldGetAllCandidates() {
        when(candidateService.getAllCandidates()).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getAllCandidates().block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getAllCandidates();
    }

    @Test
    void shouldGetCandidateById() {
        when(candidateService.getCandidateById("1")).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidatesController.getCandidateById("1").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).getCandidateById("1");
    }

    @Test
    void shouldUpdateCandidate(){
        when(candidateService.updateCandidate(eq(candidate.getId()), any(Candidate.class))).thenReturn(candidate);

        ResponseEntity<?> response = candidatesController.updateCandidate(candidate.getId(), candidate).block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).updateCandidate(eq(candidate.getId()), any(Candidate.class));
    }

    @Test
    void shouldGetCandidateByName() {
        when(candidateService.getCandidateByName("John Doe")).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getCandidateByName("John Doe").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getCandidateByName("John Doe");
    }

    @Test
    void shouldGetCandidatesByAssignedTo() {
        when(candidateService.findCandidatesByAssignedTo("1")).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getCandidatesAssignedToDeveloper("1").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).findCandidatesByAssignedTo("1");
    }

    @Test
    void shouldDeleteCandidate() {
        doNothing().when(candidateService).deleteCandidate("1");

        ResponseEntity<Void> response = candidatesController.deleteCandidate("1").block();

        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(candidateService).deleteCandidate("1");
    }

    @Test
    void shouldGetCandidatesByPositionId() {
        when(candidateService.getCandidatesByPositionId("1")).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getCandidatesByPositionId("1").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getCandidatesByPositionId("1");
    }

    @Test
    void shouldFindCandidateByDocumentId() {
        when(candidateService.findCandidateByDocumentId("1")).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidatesController.findCandidateByDocumentId("1").block();

        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).findCandidateByDocumentId("1");
    }

    @Test
    void shouldHireCandidate() {
        doNothing().when(candidateService).hireCandidate("1", "1");

        ResponseEntity<Void> response = candidatesController.hireCandidate("1", "1").block();

        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(candidateService).hireCandidate("1", "1");
    }
}
