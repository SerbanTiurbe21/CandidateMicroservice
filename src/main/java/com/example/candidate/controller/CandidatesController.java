package com.example.candidate.controller;

import com.example.candidate.model.Candidate;
import com.example.candidate.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidates")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CandidatesController {

    private final CandidateService candidateService;

    @Operation(summary = "Add a new candidate", description = "Creates a new candidate with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Candidate created successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<Candidate> addCandidate(
            @Parameter(description = "Candidate object containing the necessary information to create a new candidate record. This includes personal details, qualifications, and any other relevant information.")
            @Valid @RequestBody Candidate candidate) {
        Candidate addedCandidate = candidateService.addCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCandidate);
    }

    @Operation(summary = "Get all candidates", description = "Retrieves a l ist of all candidates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class))))
    })
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @Operation(summary = "Get a candidate by ID", description = "Retrieves a candidate by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate retrieved successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(
            @Parameter(description = "ID of the candidate to be retrieved") @PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }

    @Operation(summary = "Update a candidate", description = "Updates the details of an existing candidate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate updated successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(
            @Parameter(description = "ID of the candidate to be updated") @PathVariable String id,
            @Parameter(description = "Candidate object containing the updated information") @Valid @RequestBody Candidate candidate) {
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
        return ResponseEntity.ok(updatedCandidate);
    }

    @Operation(summary = "Get candidates by position", description = "Retrieves candidates by their position")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class))))
    })
    @GetMapping(params = "position")
    public ResponseEntity<List<Candidate>> getCandidateByPosition(
            @Parameter(description = "Position of the candidates to be retrieved") @RequestParam String position) {
        return ResponseEntity.ok(candidateService.getCandidateByPosition(position));
    }

    @Operation(summary = "Get candidates by name", description = "Retrieves candidates by their name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class))))
    })
    @GetMapping(params = "name")
    public ResponseEntity<List<Candidate>> getCandidateByName(
            @Parameter(description = "Name of the candidates to be retrieved") @RequestParam String name) {
        return ResponseEntity.ok(candidateService.getCandidateByName(name));
    }

}
