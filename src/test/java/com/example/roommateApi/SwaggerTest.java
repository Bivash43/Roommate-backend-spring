package com.example.roommateApi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SwaggerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSwagger() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api-docs", String.class);
        System.out.println("Status Code: " + response.getStatusCode().value());
        System.out.println("Body: " + response.getBody());
        if (response.getStatusCode().is5xxServerError()) {
            throw new RuntimeException("500 Error: " + response.getBody());
        }
    }
}
