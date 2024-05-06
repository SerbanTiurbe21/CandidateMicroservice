package com.example.candidate.service;

import com.example.candidate.exception.CandidateNotFoundException;
import com.example.candidate.exception.DuplicateCandidateException;
import com.example.candidate.exception.PositionNotFoundException;
import com.example.candidate.model.Candidate;
import com.example.candidate.model.Position;
import com.example.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final PositionsService positionsService;

    public Candidate addCandidate(Candidate candidate) {
        Position position = positionsService.getPositionById(candidate.getPositionId());
        String positionName = position.getName();
        if (positionsService.getPositionsByName(positionName).isEmpty()) {
            throw new PositionNotFoundException("Position with id " + candidate.getPositionId() + " not found");
        }
        if (candidateRepository.existsByPhoneNumber(candidate.getPhoneNumber())){
            throw new DuplicateCandidateException("Candidate with this phone number already exist");
        }
        if (candidateRepository.existsByEmail(candidate.getEmail())){
            throw new DuplicateCandidateException("Candidate with this email already exist");
        }
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return candidateRepository.findAll(sort);
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not exist with id: " + id));
    }

    public Candidate updateCandidate(String id, Candidate updatedCandidate) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate with " + id + " not found"));

        candidate.setName(updatedCandidate.getName());
        candidate.setPositionId(updatedCandidate.getPositionId());
        candidate.setPhoneNumber(updatedCandidate.getPhoneNumber());
        candidate.setEmail(updatedCandidate.getEmail());
        candidate.setCvLink(updatedCandidate.getCvLink());
        candidate.setInterviewDate(updatedCandidate.getInterviewDate());
        candidate.setDocumentId(updatedCandidate.getDocumentId());
        candidate.setAssignedTo(updatedCandidate.getAssignedTo());

        return candidateRepository.save(candidate);
    }

    public List<Candidate> getCandidateByName(String name) {
        return candidateRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<Candidate> findCandidatesByAssignedTo(String assignedToId) {
        List<Candidate> assignedCandidates = candidateRepository.findCandidatesByAssignedTo(assignedToId);
        if (assignedCandidates.isEmpty()) {
            throw new CandidateNotFoundException("No candidates found assigned to developer with ID: " + assignedToId);
        }
        return assignedCandidates;
    }

    @Override
    public void deleteCandidate(String id) {
        if (!candidateRepository.existsById(id)) {
            throw new CandidateNotFoundException("Candidate not exist with id: " + id);
        }
        candidateRepository.deleteById(id);
    }

    @Override
    public List<Candidate> getCandidatesByPositionId(String positionId) {
        return candidateRepository.findCandidatesByPositionId(positionId);
    }
}
