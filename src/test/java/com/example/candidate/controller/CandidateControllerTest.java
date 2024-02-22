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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {
    @Mock
    private CandidateService candidateService;
    @InjectMocks
    private CandidatesController candidatesController;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = new Candidate("1", "John Doe", "Developer", "1234567890", "http://example.com/cv", "john.doe@example.com", null, null);
    }

    @AfterEach
    public void tearDown() {
        candidate = null;
    }

    @Test
    void shouldAddCandidate() {
        when(candidateService.addCandidate(any(Candidate.class))).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidatesController.addCandidate(candidate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).addCandidate(any(Candidate.class));
    }

    @Test
    void shouldGetAllCandidates() {
        when(candidateService.getAllCandidates()).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getAllCandidates();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getAllCandidates();
    }

    @Test
    void shouldGetCandidateById() {
        when(candidateService.getCandidateById("1")).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidatesController.getCandidateById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).getCandidateById("1");
    }

    @Test
    void shouldUpdateCandidate(){
        when(candidateService.updateCandidate(eq(candidate.getId()), any(Candidate.class))).thenReturn(candidate);

        ResponseEntity<?> response = candidatesController.updateCandidate(candidate.getId(), candidate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
        verify(candidateService).updateCandidate(eq(candidate.getId()), any(Candidate.class));
    }

    @Test
    void shouldGetCandidateByPosition() {
        when(candidateService.getCandidateByPosition("Developer")).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getCandidateByPosition("Developer");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getCandidateByPosition("Developer");
    }

    @Test
    void shouldGetCandidateByName() {
        when(candidateService.getCandidateByName("John Doe")).thenReturn(List.of(candidate));

        ResponseEntity<List<Candidate>> response = candidatesController.getCandidateByName("John Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(candidate), response.getBody());
        verify(candidateService).getCandidateByName("John Doe");
    }
}
