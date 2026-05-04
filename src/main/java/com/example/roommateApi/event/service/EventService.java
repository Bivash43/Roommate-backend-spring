package com.example.roommateApi.event.service;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.event.model.Event;
import com.example.roommateApi.event.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;

    @Transactional
    public Event createEvent(Event event, Long householdId, Long creatorUserId) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found"));
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        event.setHousehold(household);
        event.getOrganizers().add(creator);

        return eventRepository.save(event);
    }

    public List<Event> getEventsByHousehold(Long householdId) {
        return eventRepository.findByHouseholdId(householdId);
    }

    @Transactional
    public void addOrganizer(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        event.getOrganizers().add(user);
        eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
