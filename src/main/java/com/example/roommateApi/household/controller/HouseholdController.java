package com.example.roommateApi.household.controller;

import com.example.roommateApi.household.dto.HouseholdRequest;
import com.example.roommateApi.household.dto.HouseholdResponse;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.service.HouseholdService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
@Tag(name = "Household Management", description = "Endpoints for managing households and their members")
public class HouseholdController {

    private final HouseholdService householdService;
    private final UserRepository userRepository;

    @Operation(summary = "Create a new household", description = "Creates a new household and assigns the creator as ADMIN")
    @ApiResponse(responseCode = "201", description = "Household created successfully")
    @PostMapping
    public ResponseEntity<HouseholdResponse> createHousehold(@Valid @RequestBody HouseholdRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Creating new household: {} by user: {}", request.name(), username);
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Household household = householdService.createHousehold(request.name(), request.address(), currentUser.getId());
        return new ResponseEntity<>(HouseholdResponse.fromEntity(household), HttpStatus.CREATED);
    }

    @Operation(summary = "Get household by ID", description = "Retrieves details of a specific household. Requires membership.")
    @ApiResponse(responseCode = "200", description = "Household details retrieved")
    @ApiResponse(responseCode = "404", description = "Household not found")
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isMemberOfHousehold(#id)")
    public ResponseEntity<HouseholdResponse> getHousehold(@PathVariable Long id) {
        log.debug("Fetching household details for ID: {}", id);
        Household household = householdService.getHousehold(id);
        return ResponseEntity.ok(HouseholdResponse.fromEntity(household));
    }

    @Operation(summary = "Get all households", description = "Retrieves a list of all households. Restricted to system administrators.")
    @ApiResponse(responseCode = "200", description = "List of households retrieved")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HouseholdResponse>> getAllHouseholds() {
        log.info("Fetching all households (Admin action)");
        List<HouseholdResponse> households = householdService.getAllHouseholds().stream()
                .map(HouseholdResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(households);
    }

    @Operation(summary = "Add user to household", description = "Adds a user to a household with a specific role. Requires household ADMIN permissions.")
    @ApiResponse(responseCode = "200", description = "User added successfully")
    @PostMapping("/{id}/members/{userId}")
    @PreAuthorize("@securityService.isHouseholdAdmin(#id)")
    public ResponseEntity<Void> addUserToHousehold(@PathVariable Long id, @PathVariable Long userId, @RequestParam(defaultValue = "MEMBER") HouseholdRole role) {
        log.info("Adding user {} to household {} with role {}", userId, id, role);
        householdService.addUserToHousehold(id, userId, role);
        return ResponseEntity.ok().build();
    }
}
