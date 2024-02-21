package com.example.candidate.service;

import com.example.candidate.model.Candidate;

import java.util.List;

public interface CandidateService {
    Candidate addCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate getCandidateById(String id);
    List<Candidate> getCandidateByPosition(String position);
    Candidate updateCandidate(String id, Candidate candidate);
    List<Candidate> getCandidateByName(String name);
}
