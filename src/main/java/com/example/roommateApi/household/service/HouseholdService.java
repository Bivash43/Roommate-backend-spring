package com.example.roommateApi.household.service;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.repository.HouseholdRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    @Transactional
    public Household createHousehold(String name, String address, Long creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorUserId));

        Household household = Household.builder()
                .name(name)
                .address(address)
                .build();

        Household savedHousehold = householdRepository.save(household);

        creator.setHousehold(savedHousehold);
        creator.setHouseholdRole(HouseholdRole.ADMIN);
        userRepository.save(creator);

        return savedHousehold;
    }

    public Household getHousehold(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with id: " + id));
    }

    public List<Household> getAllHouseholds() {
        return householdRepository.findAll();
    }

    @Transactional
    public void addUserToHousehold(Long householdId, Long userId, HouseholdRole role) {
        Household household = getHousehold(householdId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setHousehold(household);
        user.setHouseholdRole(role);
        userRepository.save(user);
    }
}
