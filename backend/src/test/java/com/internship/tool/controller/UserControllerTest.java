package com.internship.tool.controller;

import com.internship.tool.config.JwtUtil;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ Mock dependencies (VERY IMPORTANT)
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    // =========================
    // GET ALL USERS
    // =========================
    @Test
    void testGetUsers() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Page<User> page = new PageImpl<>(List.of(user));

        when(userService.getAllUsers(any())).thenReturn(page);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    // =========================
    // GET USER BY ID - SUCCESS
    // =========================
    @Test
    void testGetUser_valid() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    // =========================
    // GET USER BY ID - NOT FOUND
    // =========================
    @Test
    void testGetUser_notFound() throws Exception {

        when(userService.getUserById(999L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // DELETE USER - NOT FOUND
    // =========================
    @Test
    void testDeleteUser_notFound() throws Exception {

        doThrow(new ResourceNotFoundException("User not found"))
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // INVALID ENDPOINT
    // =========================
    @Test
    void testInvalidEndpoint() throws Exception {
        mockMvc.perform(get("/invalid-url"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // METHOD NOT ALLOWED
    // =========================
    @Test
    void testMethodNotAllowed() throws Exception {
        mockMvc.perform(put("/users"))   // ✅ NOT supported
                .andExpect(status().isMethodNotAllowed());

    }
}