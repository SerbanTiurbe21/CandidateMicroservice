package com.example.candidate.service;

import com.example.candidate.exception.CandidateNotFoundException;
import com.example.candidate.exception.DuplicateCandidateException;
import com.example.candidate.model.Candidate;
import com.example.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    public Candidate addCandidate(Candidate candidate) {
        if (candidateRepository.existsByPhoneNumber(candidate.getPhoneNumber()))
            throw new DuplicateCandidateException("Candidate with this phone number already exist");
        if (candidateRepository.existsByEmail(candidate.getEmail()))
            throw new DuplicateCandidateException("Candidate with this email already exist");
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return candidateRepository.findAll(sort);
    }

    public List<Candidate> getCandidateByPosition(String position) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return candidateRepository.findByPosition(sort, position);
    }

    public Optional<Candidate> getCandidateById(String id) {
        return candidateRepository.findById(id);
    }

    public Candidate updateCandidate(String id, Candidate updatedCandidate) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate with " + id + " not found"));

        candidate.setName(updatedCandidate.getName());
        candidate.setPosition(updatedCandidate.getPosition());
        candidate.setPhoneNumber(updatedCandidate.getPhoneNumber());
        candidate.setEmail(updatedCandidate.getEmail());
        candidate.setCvLink(updatedCandidate.getCvLink());
        candidate.setInterviewDate(updatedCandidate.getInterviewDate());
        candidate.setDocumentId(updatedCandidate.getDocumentId());

        return candidateRepository.save(candidate);
    }

    public List<Candidate> getCandidateByName(String name) {
        return candidateRepository.findByNameIgnoreCase(name);
    }
}