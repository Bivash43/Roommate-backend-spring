package com.example.roommateApi.household.controller;

import com.example.roommateApi.household.dto.HouseholdRequest;
import com.example.roommateApi.household.dto.HouseholdResponse;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.service.HouseholdService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<HouseholdResponse> createHousehold(@Valid @RequestBody HouseholdRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Household household = householdService.createHousehold(request.name(), request.address(), currentUser.getId());
        return new ResponseEntity<>(HouseholdResponse.fromEntity(household), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseholdResponse> getHousehold(@PathVariable Long id) {
        Household household = householdService.getHousehold(id);
        return ResponseEntity.ok(HouseholdResponse.fromEntity(household));
    }

    @GetMapping
    public ResponseEntity<List<HouseholdResponse>> getAllHouseholds() {
        List<HouseholdResponse> households = householdService.getAllHouseholds().stream()
                .map(HouseholdResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(households);
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> addUserToHousehold(@PathVariable Long id, @PathVariable Long userId, @RequestParam(defaultValue = "MEMBER") HouseholdRole role) {
        householdService.addUserToHousehold(id, userId, role);
        return ResponseEntity.ok().build();
    }
}
