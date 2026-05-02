package com.example.roommateApi.auth.service;

import com.example.roommateApi.auth.dto.LoginRequest;
import com.example.roommateApi.auth.dto.SignupRequest;
import com.example.roommateApi.auth.dto.JwtResponse;
import com.example.roommateApi.role.model.Role;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.role.repository.RoleRepository;
import com.example.roommateApi.user.repository.UserRepository;
import com.example.roommateApi.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Signup user
    public String registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        Set<Role> roles = new HashSet<>();
        if (signupRequest.getRole() == null) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            roles.add(userRole);
        } else {
            roles = signupRequest.getRole().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")))
                    .collect(Collectors.toSet());
        }

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);
        return "User registered successfully!";
    }

    // Authenticate user and generate JWT
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateJwtToken(user.getUsername());

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new JwtResponse(token, "Bearer", user.getUsername(), roles);
    }
}
