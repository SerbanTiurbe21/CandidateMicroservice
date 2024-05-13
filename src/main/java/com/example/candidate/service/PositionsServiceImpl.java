package com.example.candidate.service;

import com.example.candidate.exception.*;
import com.example.candidate.model.Candidate;
import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import com.example.candidate.repository.CandidateRepository;
import com.example.candidate.repository.PositionsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PositionsServiceImpl implements PositionsService{
    private final PositionsRepository positionsRepository;
    private final CandidateRepository candidateRepository;
    @Override
    public List<Position> getAllPositions() {
        return positionsRepository.findAll();
    }

    @Override
    public Position getPositionById(String id) {
        return positionsRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));
    }

    @Override
    public List<Position> getPositionsByName(String name) {
        return positionsRepository.findPositionsByName(name);
    }

    @Override
    public Position addPosition(Position position) {
        List<Position> existingPositions = positionsRepository.findPositionsByName(position.getName());
        if (!existingPositions.isEmpty()) {
            for (Position existingPosition : existingPositions) {
                if (!existingPosition.getStatus().equals(Status.CLOSED)) {
                    throw new PositionAlreadyExistsException("Position with name " + position.getName() + " already exists and is not closed");
                }
            }
        }

        position.setStatus(Status.OPEN);
        position.setSubStatus(null);
        return positionsRepository.save(position);
    }

    @Override
    public Position updatePosition(String id, Position position) {
        Position positionToUpdate = positionsRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));
        positionToUpdate.setName(position.getName());
        positionToUpdate.setStatus(position.getStatus());
        if(position.getStatus().equals(Status.CLOSED)){
            if(position.getSubStatus() == null){
                position.setSubStatus(null);
            }
            positionToUpdate.setSubStatus(position.getSubStatus());
        }
        if(position.getStatus().equals(Status.OPEN) && position.getSubStatus() == null){
            positionToUpdate.setSubStatus(null);
        }
        return positionsRepository.save(positionToUpdate);
    }

    @Override
    public void cancelPosition(String id) {
        Position position = positionsRepository.findById(id)
                .orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));

        ensurePositionIsOpen(position);
        ensureNoActiveCandidates(id);

        position.setStatus(Status.CLOSED);
        position.setSubStatus(SubStatus.CANCELLED);
        positionsRepository.save(position);
    }

    @Override
    public void fillPosition(String id, String hiredCandidateId) {
        Position position = positionsRepository.findById(id)
                .orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));

        ensurePositionIsOpen(position);
        Candidate candidate = candidateRepository.findById(hiredCandidateId)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate with id " + hiredCandidateId + " not found"));

        position.setStatus(Status.CLOSED);
        position.setSubStatus(SubStatus.FILLED);
        position.setHiredCandidateId(hiredCandidateId);
        positionsRepository.save(position);

        candidate.setHired(true);
        candidate.setAssignedTo(null);
        candidateRepository.save(candidate);
    }

    @Override
    public List<Position> getPositionsByStatus(Status status) {
        return positionsRepository.findPositionsByStatus(status);
    }

    @Override
    public List<Position> getPositionsByStatusAndSubStatus(@NonNull Status status, SubStatus subStatus) {
        return positionsRepository.findPositionsByStatusAndSubStatus(status, subStatus);
    }

    @Override
    public List<Position> getPositionsByStatuses(Status status1, Status status2) {
        return positionsRepository.findPositionsByStatuses(status1, status2);
    }

    private void ensurePositionIsOpen(Position position) {
        if (position.getStatus() == Status.CLOSED) {
            throw new PositionAlreadyDeactivatedException("This position is already closed.");
        }
    }

    private void ensureNoActiveCandidates(String positionId) {
        if (candidateRepository.countCandidatesByPositionId(positionId) > 0) {
            throw new DeactivationNotAllowedException("Cannot modify position since there are active candidates linked to it.");
        }
    }
}
