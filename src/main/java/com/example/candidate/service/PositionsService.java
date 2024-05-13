package com.example.candidate.service;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

public interface PositionsService {
    List<Position> getAllPositions();
    Position getPositionById(String id);
    List<Position> getPositionsByName(String name);
    Position addPosition(Position position);
    Position updatePosition(String id, Position position);
    void cancelPosition(String id);
    void fillPosition(String id, String hiredCandidateId);
    List<Position> getPositionsByStatus(Status status);
    List<Position> getPositionsByStatusAndSubStatus(@NonNull Status status, SubStatus subStatus);
    List<Position> getPositionsByStatuses(Status status1, Status status2);
    Set<String> getUniquePositionNames();
}
