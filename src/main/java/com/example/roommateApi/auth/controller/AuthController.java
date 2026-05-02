package com.example.roommateApi.auth.controller;

import com.example.roommateApi.auth.dto.LoginRequest;
import com.example.roommateApi.auth.dto.SignupRequest;
import com.example.roommateApi.auth.dto.JwtResponse;
import com.example.roommateApi.auth.service.AuthService;
import com.example.roommateApi.core.handler.GlobalApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user signup and login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<GlobalApiResponse<String>> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String result = authService.registerUser(signupRequest);
            return ResponseEntity.ok(new GlobalApiResponse<>(true, result, "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GlobalApiResponse<>(false, null, e.getMessage()));
        }
    }


    @Operation(summary = "Login user and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<GlobalApiResponse<JwtResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new GlobalApiResponse<>(true, jwtResponse, "Login successful"));
    }
}
