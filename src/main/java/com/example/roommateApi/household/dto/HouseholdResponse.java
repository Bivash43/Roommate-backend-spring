package com.example.roommateApi.household.dto;

import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.user.dto.UserResponse;

import java.util.Set;
import java.util.stream.Collectors;

public record HouseholdResponse(
    Long id,
    String name,
    String address,
    Set<UserResponse> members
) {
    public static HouseholdResponse fromEntity(Household household) {
        return new HouseholdResponse(
            household.getId(),
            household.getName(),
            household.getAddress(),
            household.getMembers().stream()
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
