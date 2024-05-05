package com.example.candidate.service;

import com.example.candidate.model.Position;
import com.example.candidate.model.Status;
import com.example.candidate.model.SubStatus;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PositionsService {
    List<Position> getAllPositions();
    Position getPositionById(String id);
    Position getPositionByName(String name);
    Position addPosition(Position position);
    Position updatePosition(String id, Position position);
    void cancelPosition(String id);
    void fillPosition(String id, String hiredCandidateId);
    List<Position> getPositionsByStatus(Status status);
    List<Position> getPositionsByStatusAndSubStatus(@NonNull Status status, SubStatus subStatus);
}
