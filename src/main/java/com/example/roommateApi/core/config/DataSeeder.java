package com.example.roommateApi.core.config;

import com.example.roommateApi.role.model.Role;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.role.repository.RoleRepository;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        seedRole("USER");
        seedRole("ADMIN");
        seedRole("SUPER_ADMIN");

        // Seed SuperAdmin User
        if (!userRepository.existsByUsername("superadmin")) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setEmail("superadmin@roommate.com");
            superAdmin.setPassword(passwordEncoder.encode("superadmin"));

            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("SUPER_ADMIN").ifPresent(roles::add);
            superAdmin.setRoles(roles);

            userRepository.save(superAdmin);
            System.out.println("SuperAdmin user seeded successfully.");
        }
    }

    private void seedRole(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(null, roleName));
        }
    }
}
