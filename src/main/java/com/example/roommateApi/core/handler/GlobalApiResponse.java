package com.example.roommateApi.core.handler;

import lombok.Getter;

import java.time.LocalDateTime;

// Success Response Wrapper
@Getter
public class GlobalApiResponse<T> {
    // Getters
    private final boolean success;
    private final T data;
    private final String message;
    private final LocalDateTime timestamp;

    public GlobalApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
