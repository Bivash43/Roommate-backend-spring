package com.example.roommateApi.chore.service;

import com.example.roommateApi.chore.model.Chore;
import com.example.roommateApi.chore.model.ChoreStatus;
import com.example.roommateApi.chore.repository.ChoreRepository;
import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.repository.HouseholdRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    @Transactional
    public Chore createChore(Chore chore, Long householdId, Long assignedUserId) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found"));
        
        if (assignedUserId != null) {
            User user = userRepository.findById(assignedUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            chore.setAssignedUser(user);
        }

        chore.setHousehold(household);
        return choreRepository.save(chore);
    }

    public List<Chore> getChoresByHousehold(Long householdId) {
        return choreRepository.findByHouseholdId(householdId);
    }

    @Transactional
    public Chore updateChoreStatus(Long choreId, ChoreStatus status) {
        Chore chore = choreRepository.findById(choreId)
                .orElseThrow(() -> new ResourceNotFoundException("Chore not found"));
        chore.setStatus(status);
        return choreRepository.save(chore);
    }

    @Transactional
    public void deleteChore(Long id) {
        choreRepository.deleteById(id);
    }
}
