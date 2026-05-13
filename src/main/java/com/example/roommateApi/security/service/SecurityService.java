package com.example.roommateApi.security.service;

import com.example.roommateApi.chore.repository.ChoreRepository;
import com.example.roommateApi.event.repository.EventRepository;
import com.example.roommateApi.schedule.repository.ScheduleRepository;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for handling domain-specific security checks.
 * Used primarily in @PreAuthorize annotations to enforce fine-grained access control.
 */
@Slf4j
@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ChoreRepository choreRepository;
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;

    /**
     * Checks if the current user is a member of the specified household.
     * @param householdId the ID of the household to check
     * @return true if the user is a member, false otherwise
     */
    public boolean isMemberOfHousehold(Long householdId) {
        User currentUser = getCurrentUser();
        return currentUser.getHousehold() != null && Objects.equals(currentUser.getHousehold().getId(), householdId);
    }

    /**
     * Checks if the current user belongs to the same household as the specified chore.
     * @param choreId the ID of the chore to check
     * @return true if the user and chore are in the same household, false otherwise
     */
    public boolean isChoreOwner(Long choreId) {
        User currentUser = getCurrentUser();
        return choreRepository.findById(choreId)
                .map(chore -> Objects.equals(chore.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user belongs to the same household as the specified schedule.
     * @param scheduleId the ID of the schedule to check
     * @return true if the user and schedule are in the same household, false otherwise
     */
    public boolean isScheduleOwner(Long scheduleId) {
        User currentUser = getCurrentUser();
        return scheduleRepository.findById(scheduleId)
                .map(schedule -> Objects.equals(schedule.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user belongs to the same household as the specified event.
     * @param eventId the ID of the event to check
     * @return true if the user and event are in the same household, false otherwise
     */
    public boolean isEventOwner(Long eventId) {
        User currentUser = getCurrentUser();
        return eventRepository.findById(eventId)
                .map(event -> Objects.equals(event.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user is an ADMIN of the specified household.
     * @param householdId the ID of the household to check
     * @return true if the user is a household admin, false otherwise
     */
    public boolean isHouseholdAdmin(Long householdId) {
        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getHousehold() != null 
                && Objects.equals(currentUser.getHousehold().getId(), householdId)
                && currentUser.getHouseholdRole() == HouseholdRole.ADMIN;
        
        if (!isAdmin) {
            log.warn("User {} attempted to perform admin action on household {} without sufficient permissions", 
                    currentUser.getUsername(), householdId);
        }
        return isAdmin;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
