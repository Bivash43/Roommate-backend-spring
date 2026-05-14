package com.example.roommateApi.household.service;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.repository.HouseholdRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing households and their members.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new household and assigns the creator as an ADMIN.
     * @param name the name of the household
     * @param address the physical address of the household
     * @param creatorUserId the ID of the user creating the household
     * @return the created household
     */
    @Transactional
    public Household createHousehold(String name, String address, Long creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorUserId));

        Household household = Household.builder()
                .name(name)
                .address(address)
                .build();

        Household savedHousehold = householdRepository.save(household);

        log.info("Assigning user {} as ADMIN of new household {}", creatorUserId, savedHousehold.getId());
        creator.setHousehold(savedHousehold);
        creator.setHouseholdRole(HouseholdRole.ADMIN);
        userRepository.save(creator);

        return savedHousehold;
    }

    /**
     * Retrieves a household by its ID.
     * @param id the ID of the household to retrieve
     * @return the found household
     * @throws ResourceNotFoundException if the household is not found
     */
    public Household getHousehold(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with id: " + id));
    }

    /**
     * Retrieves all households in the system.
     * @return a list of all households
     */
    public List<Household> getAllHouseholds() {
        return householdRepository.findAll();
    }

    /**
     * Adds a user to a household with a specific role.
     * @param householdId the ID of the household
     * @param userId the ID of the user to add
     * @param role the role to assign to the user in the household
     */
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
