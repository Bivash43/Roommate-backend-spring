package com.example.roommateApi.auth.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Data
public class SignupRequest {
    @Schema(example = "john")
    private String username;

    @Schema(example = "john@example.com")
    private String email;

    @Schema(example = "1234")
    private String password;

    @Schema(example = "[\"USER\"]")
    private Set<String> role;
}


