package com.example.roommateApi.household.controller;

import com.example.roommateApi.household.dto.HouseholdRequest;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.household.service.HouseholdService;
import com.example.roommateApi.security.service.SecurityService;
import com.example.roommateApi.user.model.User;
import com.example.roommateApi.user.repository.UserRepository;
import com.example.roommateApi.security.jwt.JwtAuthenticationFilter;
import com.example.roommateApi.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseholdController.class)
@Import(HouseholdControllerTest.TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false) // Disable filters to focus on Method Security
class HouseholdControllerTest {

    @org.springframework.boot.test.context.TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HouseholdService householdService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean(name = "securityService")
    private SecurityService securityService;

    // These need to be mocked because they are likely required by the context or some auto-config
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void createHousehold_Success() throws Exception {
        HouseholdRequest request = new HouseholdRequest("My Home", "123 Street");
        Household household = Household.builder().id(1L).name("My Home").build();
        User user = User.builder().id(1L).username("testuser").build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(householdService.createHousehold(any(), any(), anyLong())).thenReturn(household);

        mockMvc.perform(post("/api/households")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("My Home"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getHousehold_AccessDenied() throws Exception {
        when(securityService.isMemberOfHousehold(1L)).thenReturn(false);

        mockMvc.perform(get("/api/households/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getHousehold_Success() throws Exception {
        Household household = Household.builder().id(1L).name("My Home").build();
        when(securityService.isMemberOfHousehold(1L)).thenReturn(true);
        when(householdService.getHousehold(1L)).thenReturn(household);

        mockMvc.perform(get("/api/households/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Home"));
    }

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void getAllHouseholds_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/households"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllHouseholds_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/households"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "householdAdmin")
    void addUserToHousehold_Success() throws Exception {
        when(securityService.isHouseholdAdmin(1L)).thenReturn(true);

        mockMvc.perform(post("/api/households/1/members/2")
                        .param("role", "MEMBER"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "notAdmin")
    void addUserToHousehold_Forbidden() throws Exception {
        when(securityService.isHouseholdAdmin(1L)).thenReturn(false);

        mockMvc.perform(post("/api/households/1/members/2")
                        .param("role", "MEMBER"))
                .andExpect(status().isForbidden());
    }
}
