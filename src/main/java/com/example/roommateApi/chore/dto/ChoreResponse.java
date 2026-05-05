package com.example.roommateApi.chore.dto;

import com.example.roommateApi.chore.model.Chore;
import com.example.roommateApi.chore.model.ChoreStatus;
import com.example.roommateApi.user.dto.UserResponse;

import java.time.LocalDate;
import java.util.stream.Collectors;

public record ChoreResponse(
    Long id,
    String title,
    String description,
    LocalDate dueDate,
    ChoreStatus status,
    UserResponse assignedUser
) {
    public static ChoreResponse fromEntity(Chore chore) {
        return new ChoreResponse(
            chore.getId(),
            chore.getTitle(),
            chore.getDescription(),
            chore.getDueDate(),
            chore.getStatus(),
            chore.getAssignedUser() != null ? new UserResponse(
                chore.getAssignedUser().getId(),
                chore.getAssignedUser().getUsername(),
                chore.getAssignedUser().getEmail(),
                chore.getAssignedUser().getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()),
                chore.getAssignedUser().getHouseholdRole() != null ? chore.getAssignedUser().getHouseholdRole().name() : null
            ) : null
        );
    }
}
