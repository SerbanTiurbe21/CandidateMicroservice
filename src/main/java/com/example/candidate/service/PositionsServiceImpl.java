package com.example.candidate.service;

import com.example.candidate.exception.PositionAlreadyExistsException;
import com.example.candidate.exception.PositionNotFoundException;
import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import com.example.candidate.repository.PositionsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionsServiceImpl implements PositionsService{
    private final PositionsRepository positionsRepository;
    @Override
    public List<Position> getAllPositions() {
        return positionsRepository.findAll();
    }

    @Override
    public Position getPositionById(String id) {
        return positionsRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));
    }

    @Override
    public Position getPositionByName(String name) {
       return positionsRepository.findByName(name).orElseThrow(() -> new PositionNotFoundException("Position with name " + name + " not found"));
    }

    @Override
    public Position addPosition(Position position) {
        Optional<Position> existingPositionOptional = positionsRepository.findByName(position.getName());

        if (existingPositionOptional.isPresent()) {
            Position existingPosition = existingPositionOptional.get();
            if (!existingPosition.getStatus().equals(Status.CLOSED)) {
                throw new PositionAlreadyExistsException("Position with name " + position.getName() + " already exists and is not closed");
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
            positionToUpdate.setSubStatus(position.getSubStatus());
        }
        return positionsRepository.save(positionToUpdate);
    }

    @Override
    public void deactivatePosition(String id, SubStatus reason) {
        Position position = positionsRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));
        position.setStatus(Status.CLOSED);
        position.setSubStatus(reason);
        positionsRepository.save(position);
    }

    @Override
    public List<Position> getPositionsByStatus(Status status) {
        return positionsRepository.findPositionsByStatus(status);
    }

    @Override
    public List<Position> getPositionsByStatusAndSubStatus(@NonNull Status status, SubStatus subStatus) {
        return positionsRepository.findPositionsByStatusAndSubStatus(status, subStatus);
    }
}
