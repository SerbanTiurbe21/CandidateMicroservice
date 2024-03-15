package com.example.candidate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "candidates")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {
    @Id
    private String id;
    @Size(min = 3, message = "Please provide a valid name")
    private String name;
    private String position;
    @Pattern(regexp = "^[+]?[0-9]{10,13}$", message = "Please provide a valid phone number")
    private String phoneNumber;
    @Pattern(regexp = "((https?://)([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?)?", message = "Please provide a valid link")
    private String cvLink;
    @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    private String email;
    private LocalDate interviewDate;
    private String documentId;
    private String assignedTo;
}
