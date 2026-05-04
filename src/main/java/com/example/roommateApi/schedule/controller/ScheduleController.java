package com.example.roommateApi.schedule.controller;

import com.example.roommateApi.schedule.model.Schedule;
import com.example.roommateApi.schedule.service.ScheduleService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getHousehold() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Schedule savedSchedule = scheduleService.createSchedule(schedule, currentUser.getHousehold().getId(), currentUser.getId());
        return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<Schedule>> getSchedulesByHousehold(@PathVariable Long householdId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByHousehold(householdId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
