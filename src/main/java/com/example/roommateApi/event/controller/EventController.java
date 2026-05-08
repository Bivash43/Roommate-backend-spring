package com.example.roommateApi.event.controller;

import com.example.roommateApi.event.dto.EventRequest;
import com.example.roommateApi.event.dto.EventResponse;
import com.example.roommateApi.event.model.Event;
import com.example.roommateApi.event.service.EventService;
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
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getHousehold() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .eventDate(request.eventDate())
                .build();

        Event savedEvent = eventService.createEvent(event, currentUser.getHousehold().getId(), currentUser.getId());
        return new ResponseEntity<>(EventResponse.fromEntity(savedEvent), HttpStatus.CREATED);
    }

    @GetMapping("/household/{householdId}")
    @PreAuthorize("@securityService.isMemberOfHousehold(#householdId)")
    public ResponseEntity<List<EventResponse>> getEventsByHousehold(@PathVariable Long householdId) {
        List<EventResponse> events = eventService.getEventsByHousehold(householdId).stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{id}/organizers/{userId}")
    @PreAuthorize("@securityService.isEventOwner(#id)")
    public ResponseEntity<Void> addOrganizer(@PathVariable Long id, @PathVariable Long userId) {
        eventService.addOrganizer(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isEventOwner(#id)")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
