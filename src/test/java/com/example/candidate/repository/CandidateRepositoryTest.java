package com.example.candidate.repository;

import com.example.candidate.model.Candidate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    private Candidate candidate, candidate1;

    @BeforeEach
    public void setUp() {
        candidate = new Candidate("1", "John Doe", "1234567890", "http://example.com/cv", "john.doe@example.com", null, null, null, null, false);
        candidate1 = new Candidate("2", "Jane Doe", "1234567890", "http://example.com/cv", "jane.doe@example.com", null, null, null, null, false);
        candidateRepository.saveAll(List.of(candidate, candidate1));
    }

    @AfterEach
    public void tearDown() {
        candidateRepository.delete(candidate);
        candidateRepository.delete(candidate1);
    }

    @Test
    void shouldCheckIfCandidateExistsByPhoneNumber() {
        boolean exists = candidateRepository.existsByPhoneNumber("1234567890");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckIfCandidateExistsByEmail() {
        boolean exists = candidateRepository.existsByEmail("jane.doe@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldFindCandidateByNameIgnoreCase() {
        List<Candidate> candidates = candidateRepository.findByNameIgnoreCase("john doe");
        assertThat(candidates).hasSize(1);
    }

    @Test
    void shouldFindAllCandidates() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Candidate> candidates = candidateRepository.findAll(sort);
        assertThat(candidates).hasSize(2);
    }

    @Test
    void shouldFindCandidatesByAssignedTo() {
        List<Candidate> candidates = candidateRepository.findCandidatesByAssignedTo("1");
        assertThat(candidates).isEmpty();
    }

    @Test
    void shouldFindCandidatesByPositionId() {
        List<Candidate> candidates = candidateRepository.findCandidatesByPositionId("1");
        assertThat(candidates).isEmpty();
    }

    @Test
    void shouldCountCandidatesByPositionId() {
        int count = candidateRepository.countCandidatesByPositionId("1");
        assertThat(count).isZero();
    }

    @Test
    void shouldFindCandidateByDocumentId() {
        Candidate candidate = candidateRepository.findCandidateByDocumentId("1");
        assertThat(candidate).isNull();
    }
}
