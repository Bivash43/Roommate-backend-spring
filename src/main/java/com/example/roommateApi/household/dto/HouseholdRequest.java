package com.example.roommateApi.household.dto;

import jakarta.validation.constraints.NotBlank;

public record HouseholdRequest(
    @NotBlank(message = "Household name is required")
    String name,
    
    String address
) {}
