package com.example.roommateApi.schedule.service;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.repository.HouseholdRepository;
import com.example.roommateApi.schedule.model.Schedule;
import com.example.roommateApi.schedule.repository.ScheduleRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    @Transactional
    public Schedule createSchedule(Schedule schedule, Long householdId, Long userId) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        schedule.setHousehold(household);
        schedule.setUser(user);

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesByHousehold(Long householdId) {
        return scheduleRepository.findByHouseholdId(householdId);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
