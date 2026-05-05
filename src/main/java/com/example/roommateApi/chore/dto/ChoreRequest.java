package com.example.roommateApi.chore.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ChoreRequest(
    @NotBlank(message = "Chore title is required")
    String title,
    
    String description,
    
    @FutureOrPresent(message = "Due date cannot be in the past")
    LocalDate dueDate,
    
    Long assignedUserId
) {}
