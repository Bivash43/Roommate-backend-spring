package com.example.roommateApi.core.handler;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle Security: Bad Credentials
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, "Invalid username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle Security: Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, "You do not have permission to access this resource");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle Validation Errors
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

    // Handle Illegal Argument / State
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleBadRequestException(RuntimeException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle generic RuntimeException
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<GlobalApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        GlobalApiResponse<Object> response = new GlobalApiResponse<>(false, null, "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Success Response helper
    public static <T> ResponseEntity<GlobalApiResponse<T>> success(T data, String message) {
        GlobalApiResponse<T> response = new GlobalApiResponse<>(true, data, message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
