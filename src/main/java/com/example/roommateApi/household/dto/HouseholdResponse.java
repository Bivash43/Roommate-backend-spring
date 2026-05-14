package com.example.roommateApi.household.dto;

import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.stream.Collectors;

public record HouseholdResponse(
    @Schema(description = "Unique identifier of the household", example = "1")
    Long id,

    @Schema(description = "The name of the household", example = "Sunset Apartments")
    String name,

    @Schema(description = "The physical address of the household", example = "123 Main St, Springfield")
    String address,

    @Schema(description = "Set of users who are members of this household")
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
