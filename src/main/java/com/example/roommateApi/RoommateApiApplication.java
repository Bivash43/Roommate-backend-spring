package com.example.roommateApi;

import com.example.roommateApi.model.Role;
import com.example.roommateApi.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoommateApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoommateApiApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(new Role(null, "USER"));
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ADMIN"));
            }
        };
    }
}
