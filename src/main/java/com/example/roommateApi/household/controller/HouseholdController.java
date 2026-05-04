package com.example.roommateApi.household.controller;

import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.service.HouseholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Household> createHousehold(@RequestBody HouseholdRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Household household = householdService.createHousehold(request.name(), request.address(), currentUser.getId());
        return new ResponseEntity<>(household, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Household> getHousehold(@PathVariable Long id) {
        return ResponseEntity.ok(householdService.getHousehold(id));
    }

    @GetMapping
    public ResponseEntity<List<Household>> getAllHouseholds() {
        return ResponseEntity.ok(householdService.getAllHouseholds());
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> addUserToHousehold(@PathVariable Long id, @PathVariable Long userId, @RequestParam(defaultValue = "MEMBER") HouseholdRole role) {
        householdService.addUserToHousehold(id, userId, role);
        return ResponseEntity.ok().build();
    }

    public record HouseholdRequest(String name, String address) {}
}
