package com.example.roommateApi.controller;

import com.example.roommateApi.dto.LoginRequest;
import com.example.roommateApi.dto.SignupRequest;
import com.example.roommateApi.dto.JwtResponse;
import com.example.roommateApi.service.AuthService;
import com.example.roommateApi.handler.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            String result = authService.registerUser(signupRequest);
            return ResponseEntity.ok(new ApiResponse<>(true, result, "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }


    @Operation(summary = "Login user and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, jwtResponse, "Login successful"));
    }
}
