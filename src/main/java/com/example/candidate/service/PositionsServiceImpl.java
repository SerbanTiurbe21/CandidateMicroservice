package com.example.candidate.service;

import com.example.candidate.exception.PositionAlreadyExistsException;
import com.example.candidate.exception.PositionNotFoundException;
import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.repository.PositionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if(positionsRepository.existsByName(position.getName())){
            throw new PositionAlreadyExistsException("Position with name " + position.getName() + " already exists");
        }
        return positionsRepository.save(position);
    }

    @Override
    public Position updatePosition(String id, Position position) {
        Position positionToUpdate = positionsRepository.findById(id).orElseThrow(() -> new PositionNotFoundException("Position with id " + id + " not found"));
        positionToUpdate.setName(position.getName());
        positionToUpdate.setStatus(position.getStatus());
        return positionsRepository.save(positionToUpdate);
    }

    @Override
    public void deletePosition(String id) {
        positionsRepository.deleteById(id);
    }

    @Override
    public List<Position> getPositionsByStatus(String status) {
        return positionsRepository.findPositionsByStatus(Status.valueOf(status));
    }
}
