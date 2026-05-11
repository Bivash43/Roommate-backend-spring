package com.example.roommateApi.household.service;

import com.example.roommateApi.core.exception.ResourceNotFoundException;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.model.HouseholdRole;
import com.example.roommateApi.household.repository.HouseholdRepository;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseholdServiceTest {

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HouseholdService householdService;

    private User testUser;
    private Household testHousehold;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        testHousehold = Household.builder()
                .id(1L)
                .name("Test Home")
                .address("123 Street")
                .build();
    }

    @Test
    void createHousehold_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(householdRepository.save(any(Household.class))).thenReturn(testHousehold);

        Household result = householdService.createHousehold("Test Home", "123 Street", 1L);

        assertNotNull(result);
        assertEquals("Test Home", result.getName());
        assertEquals(testHousehold, testUser.getHousehold());
        assertEquals(HouseholdRole.ADMIN, testUser.getHouseholdRole());
        verify(userRepository).save(testUser);
    }

    @Test
    void createHousehold_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            householdService.createHousehold("Test Home", "123 Street", 1L));
    }

    @Test
    void getHousehold_Success() {
        when(householdRepository.findById(1L)).thenReturn(Optional.of(testHousehold));

        Household result = householdService.getHousehold(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getHousehold_NotFound_ThrowsException() {
        when(householdRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> householdService.getHousehold(1L));
    }

    @Test
    void addUserToHousehold_Success() {
        User newUser = User.builder().id(2L).username("newuser").build();
        when(householdRepository.findById(1L)).thenReturn(Optional.of(testHousehold));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));

        householdService.addUserToHousehold(1L, 2L, HouseholdRole.MEMBER);

        assertEquals(testHousehold, newUser.getHousehold());
        assertEquals(HouseholdRole.MEMBER, newUser.getHouseholdRole());
        verify(userRepository).save(newUser);
    }
}
