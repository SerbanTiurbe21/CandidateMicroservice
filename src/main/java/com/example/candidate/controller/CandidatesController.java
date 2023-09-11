package com.example.candidate.controller;

import com.example.candidate.exception.CandidateNotFoundException;
import com.example.candidate.model.Candidate;
import com.example.candidate.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidates")
public class CandidatesController {

    private final CandidateService candidateService;

    @PostMapping()
    public ResponseEntity<?> addCandidate(@Valid @RequestBody Candidate candidate, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Validation failed");
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Candidate addedCandidate = candidateService.addCandidate(candidate);
        return new ResponseEntity<>(addedCandidate, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return new ResponseEntity<>(candidateService.getAllCandidates(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable String id) {
        Optional<Candidate> candidateOptional = candidateService.getCandidateById(id);
        if (candidateOptional.isPresent()) {
            return ResponseEntity.ok(candidateOptional.get());
        } else {
            throw new CandidateNotFoundException("Candidate not exist with id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(@PathVariable String id, @Valid @RequestBody Candidate candidate, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Validation failed");
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }

    @GetMapping(params = "position")
    public ResponseEntity<List<Candidate>> getCandidateByPosition(@RequestParam String position) {
        return ResponseEntity.ok(candidateService.getCandidateByPosition(position));
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<Candidate>> getCandidateByName(@RequestParam String name){
        return ResponseEntity.ok(candidateService.getCandidateByName(name));
    }

}
