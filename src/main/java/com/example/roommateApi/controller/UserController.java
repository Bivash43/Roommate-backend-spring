package com.example.roommateApi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
@SecurityRequirement(name = "bearerAuth") // Secured with JWT
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user's info")
    public ResponseEntity<String> getCurrentUser() {
        // Just for demo; you can inject principal to get user details
        return ResponseEntity.ok("You are authenticated and can see this endpoint!");
    }

    @GetMapping("/admin")
    @Operation(summary = "Admin-only endpoint")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Admin-only access granted!");
    }
}
