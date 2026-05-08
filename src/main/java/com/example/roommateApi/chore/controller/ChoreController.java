package com.example.roommateApi.chore.controller;

import com.example.roommateApi.chore.dto.ChoreRequest;
import com.example.roommateApi.chore.dto.ChoreResponse;
import com.example.roommateApi.chore.model.Chore;
import com.example.roommateApi.chore.model.ChoreStatus;
import com.example.roommateApi.chore.service.ChoreService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chores")
@RequiredArgsConstructor
public class ChoreController {

    private final ChoreService choreService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ChoreResponse> createChore(@Valid @RequestBody ChoreRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getHousehold() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Chore chore = Chore.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .build();

        Chore savedChore = choreService.createChore(chore, currentUser.getHousehold().getId(), request.assignedUserId());
        return new ResponseEntity<>(ChoreResponse.fromEntity(savedChore), HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    @PreAuthorize("@securityService.isMemberOfHousehold(#householdId)")
    public ResponseEntity<List<ChoreResponse>> getChoresByHousehold(@PathVariable Long householdId) {
        List<ChoreResponse> chores = choreService.getChoresByHousehold(householdId).stream()
                .map(ChoreResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chores);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("@securityService.isChoreOwner(#id)")
    public ResponseEntity<ChoreResponse> updateStatus(@PathVariable Long id, @RequestParam ChoreStatus status) {
        Chore chore = choreService.updateChoreStatus(id, status);
        return ResponseEntity.ok(ChoreResponse.fromEntity(chore));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isChoreOwner(#id)")
    public ResponseEntity<Void> deleteChore(@PathVariable Long id) {
        choreService.deleteChore(id);
        return ResponseEntity.noContent().build();
    }
}
