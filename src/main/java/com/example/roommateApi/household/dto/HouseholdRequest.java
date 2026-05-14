package com.example.roommateApi.household.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record HouseholdRequest(
    @Schema(description = "The name of the household", example = "Sunset Apartments")
    @NotBlank(message = "Household name is required")
    String name,
    
    @Schema(description = "The physical address of the household", example = "123 Main St, Springfield")
    String address
) {}
