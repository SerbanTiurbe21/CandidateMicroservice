package com.example.candidate.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Position {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private Status status;
    private SubStatus subStatus;
    private String hiredCandidateId;
}
