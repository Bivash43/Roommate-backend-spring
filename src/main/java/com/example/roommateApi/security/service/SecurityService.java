package com.example.roommateApi.security.service;

import com.example.roommateApi.chore.repository.ChoreRepository;
import com.example.roommateApi.event.repository.EventRepository;
import com.example.roommateApi.schedule.repository.ScheduleRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ChoreRepository choreRepository;
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;

    public boolean isMemberOfHousehold(Long householdId) {
        User currentUser = getCurrentUser();
        return currentUser.getHousehold() != null && Objects.equals(currentUser.getHousehold().getId(), householdId);
    }

    public boolean isChoreOwner(Long choreId) {
        User currentUser = getCurrentUser();
        return choreRepository.findById(choreId)
                .map(chore -> Objects.equals(chore.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    public boolean isScheduleOwner(Long scheduleId) {
        User currentUser = getCurrentUser();
        return scheduleRepository.findById(scheduleId)
                .map(schedule -> Objects.equals(schedule.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    public boolean isEventOwner(Long eventId) {
        User currentUser = getCurrentUser();
        return eventRepository.findById(eventId)
                .map(event -> Objects.equals(event.getHousehold().getId(), currentUser.getHousehold().getId()))
                .orElse(false);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
