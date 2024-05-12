package com.example.candidate.service;

import com.example.candidate.model.Candidate;

import java.util.List;

public interface CandidateService {
    Candidate addCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate getCandidateById(String id);
    Candidate updateCandidate(String id, Candidate candidate);
    List<Candidate> getCandidateByName(String name);
    List<Candidate> findCandidatesByAssignedTo(String assignedToId);
    void deleteCandidate(String id);
    List<Candidate> getCandidatesByPositionId(String positionId);
    Candidate findCandidateByDocumentId(String documentId);
    void hireCandidate(String id, String positionId);
}
