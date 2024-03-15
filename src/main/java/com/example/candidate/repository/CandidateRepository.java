package com.example.candidate.repository;

import com.example.candidate.model.Candidate;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    List<Candidate> findByPosition(Sort sort, String position);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    List<Candidate> findByNameIgnoreCase(String name);
    @NonNull List<Candidate> findAll(@NonNull Sort sort);
    List<Candidate> findCandidatesByAssignedTo(String assignedToId);
}
