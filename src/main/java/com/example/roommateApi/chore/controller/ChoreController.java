package com.example.roommateApi.chore.controller;

import com.example.roommateApi.chore.model.Chore;
import com.example.roommateApi.chore.model.ChoreStatus;
import com.example.roommateApi.chore.service.ChoreService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chores")
@RequiredArgsConstructor
public class ChoreController {

    private final ChoreService choreService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Chore> createChore(@RequestBody ChoreRequest request) {
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
        return new ResponseEntity<>(savedChore, HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<Chore>> getChoresByHousehold(@PathVariable Long householdId) {
        return ResponseEntity.ok(choreService.getChoresByHousehold(householdId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Chore> updateStatus(@PathVariable Long id, @RequestParam ChoreStatus status) {
        return ResponseEntity.ok(choreService.updateChoreStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChore(@PathVariable Long id) {
        choreService.deleteChore(id);
        return ResponseEntity.noContent().build();
    }

    public record ChoreRequest(String title, String description, java.time.LocalDate dueDate, Long assignedUserId) {}
}
