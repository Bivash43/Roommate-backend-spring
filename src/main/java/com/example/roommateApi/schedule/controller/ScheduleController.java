package com.example.roommateApi.schedule.controller;

import com.example.roommateApi.schedule.dto.ScheduleRequest;
import com.example.roommateApi.schedule.dto.ScheduleResponse;
import com.example.roommateApi.schedule.model.Schedule;
import com.example.roommateApi.schedule.service.ScheduleService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getHousehold() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Schedule schedule = Schedule.builder()
                .resourceType(request.resourceType())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .isRecurring(request.isRecurring())
                .recurrencePattern(request.recurrencePattern())
                .build();

        Schedule savedSchedule = scheduleService.createSchedule(schedule, currentUser.getHousehold().getId(), currentUser.getId());
        return new ResponseEntity<>(ScheduleResponse.fromEntity(savedSchedule), HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    @PreAuthorize("@securityService.isMemberOfHousehold(#householdId)")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByHousehold(@PathVariable Long householdId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByHousehold(householdId).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isScheduleOwner(#id)")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
