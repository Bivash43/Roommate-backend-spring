package com.example.roommateApi.event.controller;

import com.example.roommateApi.event.model.Event;
import com.example.roommateApi.event.service.EventService;
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
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getHousehold() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Event savedEvent = eventService.createEvent(event, currentUser.getHousehold().getId(), currentUser.getId());
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<Event>> getEventsByHousehold(@PathVariable Long householdId) {
        return ResponseEntity.ok(eventService.getEventsByHousehold(householdId));
    }

    @PostMapping("/{id}/organizers/{userId}")
    public ResponseEntity<Void> addOrganizer(@PathVariable Long id, @PathVariable Long userId) {
        eventService.addOrganizer(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
