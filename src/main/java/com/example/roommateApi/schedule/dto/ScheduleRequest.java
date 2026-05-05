package com.example.roommateApi.schedule.dto;

import com.example.roommateApi.schedule.model.RecurrencePattern;
import com.example.roommateApi.schedule.model.ResourceType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleRequest(
    @NotNull(message = "Resource type is required")
    ResourceType resourceType,
    
    @NotNull(message = "Start time is required")
    LocalDateTime startTime,
    
    @NotNull(message = "End time is required")
    LocalDateTime endTime,
    
    boolean isRecurring,
    
    @NotNull(message = "Recurrence pattern is required")
    RecurrencePattern recurrencePattern
) {}
