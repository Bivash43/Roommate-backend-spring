package com.example.roommateApi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UserResponse(
    @Schema(description = "Unique identifier of the user", example = "1")
    Long id,

    @Schema(description = "Username used for authentication", example = "johndoe")
    String username,

    @Schema(description = "Email address of the user", example = "john@example.com")
    String email,

    @Schema(description = "Set of system roles assigned to the user", example = "[\"ROLE_USER\"]")
    Set<String> roles,

    @Schema(description = "Role within the household", example = "ADMIN")
    String householdRole
) {}
