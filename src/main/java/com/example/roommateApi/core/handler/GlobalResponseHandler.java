package com.example.roommateApi.core.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalResponseHandler {

    // Handle generic RuntimeException
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        GlobalApiResponse<Map<String, String>> response = new GlobalApiResponse<>(false, errors, "Validation failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle custom exceptions
//    @ExceptionHandler(CustomException.class)
//    @ResponseBody
//    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
//        ApiResponse<Object> response = new ApiResponse<>(false, null, ex.getMessage());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }

    // Success Response helper
    public static <T> ResponseEntity<GlobalApiResponse<T>> success(T data, String message) {
        GlobalApiResponse<T> response = new GlobalApiResponse<>(true, data, message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
