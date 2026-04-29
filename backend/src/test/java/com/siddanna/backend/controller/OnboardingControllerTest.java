package com.siddanna.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddanna.backend.model.Onboarding;
import com.siddanna.backend.repository.OnboardingRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OnboardingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OnboardingRepository repo;

    // ✅ CREATE
    @Test
    void testCreate() throws Exception {
        Onboarding obj = new Onboarding();
        obj.setName("Test");
        obj.setEmail("test" + System.currentTimeMillis() + "@gmail.com");
        obj.setRole("Dev");
        obj.setDescription("Testing");

        mockMvc.perform(post("/onboarding")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andExpect(status().isOk());
    }

    // ✅ GET ALL
    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/onboarding"))
                .andExpect(status().isOk());
    }

    // ✅ UPDATE
    @Test
    void testUpdate() throws Exception {
        Onboarding obj = new Onboarding();
        obj.setName("Old");
        obj.setEmail("old" + System.currentTimeMillis() + "@gmail.com");
        obj.setRole("Dev");
        obj.setDescription("Old");

        obj = repo.save(obj);
        obj.setName("Updated");

        mockMvc.perform(put("/onboarding/" + obj.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andExpect(status().isOk());
    }

    // ✅ DELETE
    @Test
    void testDelete() throws Exception {
        Onboarding obj = new Onboarding();
        obj.setName("Delete");
        obj.setEmail("delete" + System.currentTimeMillis() + "@gmail.com");
        obj.setRole("Dev");
        obj.setDescription("Delete");

        obj = repo.save(obj);

        mockMvc.perform(delete("/onboarding/" + obj.getId()))
                .andExpect(status().isOk());
    }

    // ❌ 404 - GET BY ID
    @Test
    void testGetById_NotFound() throws Exception {
        mockMvc.perform(get("/onboarding/999999"))
                .andExpect(status().isNotFound());
    }

    // ❌ 404 - UPDATE
    @Test
    void testUpdate_NotFound() throws Exception {
        Onboarding obj = new Onboarding();
        obj.setName("X");
        obj.setEmail("x" + System.currentTimeMillis() + "@gmail.com");
        obj.setRole("Dev");

        mockMvc.perform(put("/onboarding/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)))
                .andExpect(status().isNotFound());
    }

    // ❌ 404 - DELETE
    @Test
    void testDelete_NotFound() throws Exception {
        mockMvc.perform(delete("/onboarding/999999"))
                .andExpect(status().isNotFound());
    }

    // ❌ 400 - INVALID INPUT
    @Test
    void testCreate_InvalidInput() throws Exception {
        mockMvc.perform(post("/onboarding")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ❌ 405 - WRONG METHOD
    @Test
    void testWrongMethod() throws Exception {
        mockMvc.perform(put("/onboarding"))
                .andExpect(status().isMethodNotAllowed());
    }
}