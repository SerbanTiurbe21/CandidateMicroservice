package com.example.candidate.service;

import com.example.candidate.model.Position;

import java.util.List;

public interface PositionsService {
    List<Position> getAllPositions();
    Position getPositionById(String id);
    Position addPosition(Position position);
    Position updatePosition(String id, Position position);
    void deletePosition(String id);
}
