package com.example.roommateApi.event.repository;

import com.example.roommateApi.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByHouseholdId(Long householdId);
}
