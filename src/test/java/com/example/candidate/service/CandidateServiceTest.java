package com.example.candidate.service;

import com.example.candidate.exception.CandidateNotFoundException;
import com.example.candidate.exception.DuplicateCandidateException;
import com.example.candidate.model.Candidate;
import com.example.candidate.repository.CandidateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {
    @Mock
    private CandidateRepository candidateRepository;
    @InjectMocks
    private CandidateServiceImpl candidateService;
    private Candidate candidate;

    @BeforeEach
    void setUp() {
        candidate = new Candidate("1", "John Doe", "Developer", "1234567890", "http://example.com/cv", "john.doe@example.com", null, null, null);
    }

    @AfterEach
    void tearDown() {
        candidate = null;
    }

    @Test
    void shouldAddCandidate(){
        when(candidateRepository.existsByPhoneNumber(candidate.getPhoneNumber())).thenReturn(false);
        when(candidateRepository.existsByEmail(candidate.getEmail())).thenReturn(false);
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate addedCandidate = candidateService.addCandidate(candidate);
        assertNotNull(addedCandidate);

        verify(candidateRepository).save(candidate);
    }

    @Test
    void addCandidateShouldThrowExceptionWhenCandidateWithPhoneNumberAlreadyExist(){
        when(candidateRepository.existsByPhoneNumber(candidate.getPhoneNumber())).thenReturn(true);

        assertThrows(DuplicateCandidateException.class, () -> candidateService.addCandidate(candidate));
    }

    @Test
    void addCandidateShouldThrowExceptionWhenCandidateWithEmailAlreadyExist(){
        when(candidateRepository.existsByPhoneNumber(candidate.getPhoneNumber())).thenReturn(false);
        when(candidateRepository.existsByEmail(candidate.getEmail())).thenReturn(true);

        assertThrows(DuplicateCandidateException.class, () -> candidateService.addCandidate(candidate));
    }

    @Test
    void shouldGetAllCandidates(){
        when(candidateRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(candidate));

        List<Candidate> candidates = candidateService.getAllCandidates();
        assertFalse(candidates.isEmpty());
        assertNotNull(candidates);

        verify(candidateRepository).findAll(any(Sort.class));
    }

    @Test
    void shouldGetCandidateByPosition(){
        when(candidateRepository.findByPosition(any(Sort.class), eq(candidate.getPosition()))).thenReturn(Collections.singletonList(candidate));

        List<Candidate> candidates = candidateService.getCandidateByPosition(candidate.getPosition());
        assertFalse(candidates.isEmpty());
        assertNotNull(candidates);

        verify(candidateRepository).findByPosition(any(Sort.class), eq(candidate.getPosition()));
    }

    @Test
    void shouldGetCandidateById(){
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.ofNullable(candidate));

        Candidate foundCandidate = candidateService.getCandidateById(candidate.getId());
        assertNotNull(foundCandidate);

        verify(candidateRepository).findById(candidate.getId());
    }

    @Test
    void getCandidateByIdShouldThrowExceptionWhenCandidateNotFound() {
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> candidateService.getCandidateById(candidate.getId()));
    }


    @Test
    void shouldUpdateCandidate(){
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.ofNullable(candidate));
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        Candidate updatedCandidate = candidateService.updateCandidate(candidate.getId(), candidate);
        assertNotNull(updatedCandidate);

        verify(candidateRepository).save(candidate);
    }

    @Test
    void updateCandidateShouldThrowExceptionWhenCandidateNotFound(){
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> candidateService.updateCandidate(candidate.getId(), candidate));
    }

    @Test
    void shouldGetCandidateByName(){
        when(candidateRepository.findByNameIgnoreCase(candidate.getName())).thenReturn(Collections.singletonList(candidate));

        List<Candidate> candidates = candidateService.getCandidateByName(candidate.getName());
        assertFalse(candidates.isEmpty());
        assertNotNull(candidates);

        verify(candidateRepository).findByNameIgnoreCase(candidate.getName());
    }

    @Test
    void shouldFindCandidatesByAssignedTo(){
        when(candidateRepository.findCandidatesByAssignedTo(candidate.getAssignedTo())).thenReturn(Collections.singletonList(candidate));

        List<Candidate> candidates = candidateService.findCandidatesByAssignedTo(candidate.getAssignedTo());
        assertFalse(candidates.isEmpty());
        assertNotNull(candidates);

        verify(candidateRepository).findCandidatesByAssignedTo(candidate.getAssignedTo());
    }

    @Test
    void findCandidatesByAssignedToShouldThrowExceptionWhenNoCandidatesFound(){
        when(candidateRepository.findCandidatesByAssignedTo(candidate.getAssignedTo())).thenReturn(Collections.emptyList());

        assertThrows(CandidateNotFoundException.class, () -> candidateService.findCandidatesByAssignedTo(candidate.getAssignedTo()));
    }
}
