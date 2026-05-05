package com.example.roommateApi.schedule.dto;

import com.example.roommateApi.schedule.model.RecurrencePattern;
import com.example.roommateApi.schedule.model.ResourceType;
import com.example.roommateApi.schedule.model.Schedule;
import com.example.roommateApi.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ScheduleResponse(
    Long id,
    ResourceType resourceType,
    LocalDateTime startTime,
    LocalDateTime endTime,
    boolean isRecurring,
    RecurrencePattern recurrencePattern,
    UserResponse bookedBy
) {
    public static ScheduleResponse fromEntity(Schedule schedule) {
        return new ScheduleResponse(
            schedule.getId(),
            schedule.getResourceType(),
            schedule.getStartTime(),
            schedule.getEndTime(),
            schedule.isRecurring(),
            schedule.getRecurrencePattern(),
            new UserResponse(
                schedule.getUser().getId(),
                schedule.getUser().getUsername(),
                schedule.getUser().getEmail(),
                schedule.getUser().getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()),
                schedule.getUser().getHouseholdRole() != null ? schedule.getUser().getHouseholdRole().name() : null
            )
        );
    }
}
