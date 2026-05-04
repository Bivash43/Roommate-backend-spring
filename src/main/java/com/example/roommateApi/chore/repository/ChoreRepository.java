package com.example.roommateApi.chore.repository;

import com.example.roommateApi.chore.model.Chore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoreRepository extends JpaRepository<Chore, Long> {
    List<Chore> findByHouseholdId(Long householdId);
    List<Chore> findByAssignedUserId(Long userId);
}
