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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Candidates", description = "Operations related to candidates")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidates")
public class CandidatesController {

    private final CandidateService candidateService;

    @Operation(summary = "Add a new candidate", description = "Creates a new candidate with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Candidate created successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    @PostMapping
    public Mono<ResponseEntity<Candidate>> addCandidate(
            @Parameter(description = "Candidate object containing the necessary information to create a new candidate record. This includes personal details, qualifications, and any other relevant information.")
            @Valid @RequestBody Candidate candidate) {
        Candidate addedCandidate = candidateService.addCandidate(candidate);
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(addedCandidate));
    }

    @Operation(summary = "Get all candidates", description = "Retrieves a list of all candidates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class))))
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    @GetMapping
    public Mono<ResponseEntity<List<Candidate>>> getAllCandidates() {
        return Mono.just(ResponseEntity.ok(candidateService.getAllCandidates()));
    }

    @Operation(summary = "Get a candidate by ID", description = "Retrieves a candidate by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate retrieved successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> getCandidateById(
            @Parameter(description = "ID of the candidate to be retrieved") @PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id);
        return Mono.just(ResponseEntity.ok(candidate));
    }

    @Operation(summary = "Update a candidate", description = "Updates the details of an existing candidate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate updated successfully", content = @Content(schema = @Schema(implementation = Candidate.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> updateCandidate(
            @Parameter(description = "ID of the candidate to be updated") @PathVariable String id,
            @Parameter(description = "Candidate object containing the updated information") @Valid @RequestBody Candidate candidate) {
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
        return Mono.just(ResponseEntity.ok(updatedCandidate));
    }

    @Operation(summary = "Get candidates by name", description = "Retrieves candidates by their name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class)))),
            @ApiResponse(responseCode = "404", description = "No candidates found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @GetMapping(params = "name")
    public Mono<ResponseEntity<List<Candidate>>> getCandidateByName(
            @Parameter(description = "Name of the candidates to be retrieved") @RequestParam String name) {
        return Mono.just(ResponseEntity.ok(candidateService.getCandidateByName(name)));
    }

    @Operation(summary = "Get candidates assigned to a developer", description = "Retrieves candidates assigned to a developer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Candidate.class)))),
            @ApiResponse(responseCode = "404", description = "No candidates found")
    })
    @PreAuthorize("hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @GetMapping("/assigned/{developerId}")
    public Mono<ResponseEntity<List<Candidate>>> getCandidatesAssignedToDeveloper(@PathVariable String developerId) {
        List<Candidate> assignedCandidates = candidateService.findCandidatesByAssignedTo(developerId);
        return Mono.just(ResponseEntity.ok(assignedCandidates));
    }

    @Operation(summary = "Delete a candidate", description = "Deletes a candidate by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Candidate deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCandidate(
            @Parameter(description = "ID of the candidate to be deleted") @PathVariable String id) {
        candidateService.deleteCandidate(id);
        return Mono.just(ResponseEntity.noContent().build());
    }
    @Operation(summary = "Get candidates by position id", description = "Returns a list of candidates based on the position ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No candidates found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    @GetMapping("/position/{positionId}")
    public Mono<ResponseEntity<List<Candidate>>> getCandidatesByPositionId(
            @Parameter(description = "ID of the position to retrieve candidates") @PathVariable String positionId) {
        return Mono.just(ResponseEntity.ok(candidateService.getCandidatesByPositionId(positionId)));
    }
}
