package com.example.roommateApi.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record EventRequest(
    @NotBlank(message = "Event title is required")
    String title,
    
    String description,
    
    @Future(message = "Event date must be in the future")
    LocalDateTime eventDate
) {}
