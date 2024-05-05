package com.example.candidate.controller;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import com.example.candidate.service.PositionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Positions", description = "Operations related to positions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/positions")
public class PositionsController {
    private final PositionsService positionsService;

    @Operation(summary = "Get all positions", description = "Retrieves a list of all positions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Positions retrieved successfully")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<List<Position>>> getAllPositions() {
        return Mono.just(ResponseEntity.ok(positionsService.getAllPositions()));
    }

    @Operation(summary = "Get a position by ID", description = "Retrieves a position by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Position retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<Position>> getPositionById(@PathVariable String id) {
        return Mono.just(ResponseEntity.ok(positionsService.getPositionById(id)));
    }

    @Operation(summary = "Add a new position", description = "Creates a new position with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Position added successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<Position>> addPosition(@RequestBody @Valid Position position) {
        Position addedPosition = positionsService.addPosition(position);
        return Mono.just(ResponseEntity.ok(addedPosition));
    }

    @Operation(summary = "Update a position", description = "Updates a position with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Position updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<Position>> updatePosition(@PathVariable String id, @RequestBody @Valid Position position) {
        Position updatedPosition = positionsService.updatePosition(id, position);
        return Mono.just(ResponseEntity.ok(updatedPosition));
    }

    @Operation(summary = "Deactivate a position", description = "Deactivates a position by setting its status to INACTIVE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Position deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<Void>> deactivatePosition(@PathVariable String id, @RequestParam SubStatus reason) {
        positionsService.deactivatePosition(id, reason);
        return Mono.just(ResponseEntity.ok().build());
    }

    @Operation(summary = "Get positions by status", description = "Retrieves positions by their status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Positions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No positions found")
    })
    @GetMapping(params = "status")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<List<Position>>> getPositionsByStatus(@RequestParam Status status) {
        return Mono.just(ResponseEntity.ok(positionsService.getPositionsByStatus(status)));
    }

    @Operation(summary = "Get positions by sub-status", description = "Retrieves positions by their sub-status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Positions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No positions found")
    })
    @GetMapping("/sub-status")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-admin')")
    public Mono<ResponseEntity<List<Position>>> getPositionsByStatusAndSubStatus(@RequestParam Status status, @RequestParam SubStatus subStatus) {
        return Mono.just(ResponseEntity.ok(positionsService.getPositionsByStatusAndSubStatus(status, subStatus)));
    }

}
