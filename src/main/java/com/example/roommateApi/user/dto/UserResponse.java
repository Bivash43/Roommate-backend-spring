package com.example.roommateApi.user.dto;

import java.util.Set;

public record UserResponse(
    Long id,
    String username,
    String email,
    Set<String> roles,
    String householdRole
) {}
