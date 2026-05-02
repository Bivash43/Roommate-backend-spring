package com.example.roommateApi.auth.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class LoginRequest {
    @Schema(example = "john")
    private String username;

    @Schema(example = "1234")
    private String password;
}

