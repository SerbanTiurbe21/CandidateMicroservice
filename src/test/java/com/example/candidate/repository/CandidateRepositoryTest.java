package com.example.candidate.repository;

import com.example.candidate.model.Candidate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @BeforeEach
    public void setUp() {
        Candidate candidate = new Candidate("1", "John Doe", "Developer", "1234567890", "http://example.com/cv", "john.doe@example.com", null, null);
        Candidate candidate2 = new Candidate("2", "Jane Doe", "Developer", "1234567890", "http://example.com/cv", "jane.doe@example.com", null, null);
        candidateRepository.saveAll(List.of(candidate, candidate2));
    }

    @AfterEach
    public void tearDown() {
        candidateRepository.deleteAll();
    }

    @Test
    void shouldFindCandidateByPosition() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Candidate> candidates = candidateRepository.findByPosition(sort, "Developer");
        assertThat(candidates).hasSize(2);
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
}
