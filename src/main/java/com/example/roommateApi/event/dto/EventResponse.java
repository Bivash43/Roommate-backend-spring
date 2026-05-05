package com.example.roommateApi.event.dto;

import com.example.roommateApi.event.model.Event;
import com.example.roommateApi.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record EventResponse(
    Long id,
    String title,
    String description,
    LocalDateTime eventDate,
    Set<UserResponse> organizers
) {
    public static EventResponse fromEntity(Event event) {
        return new EventResponse(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getEventDate(),
            event.getOrganizers().stream()
                .map(user -> new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()),
                    user.getHouseholdRole() != null ? user.getHouseholdRole().name() : null
                ))
                .collect(Collectors.toSet())
        );
    }
}
