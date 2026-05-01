package com.internship.tool.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔓 This endpoint is PUBLIC → should return 200
    @Test
    void testPublicUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk()); // ✅ FIXED
    }

    // 🔓 Public endpoint but wrong HTTP method → your app returns 500
    @Test
    void testLoginWrongMethod() throws Exception {
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed()); // ✅ FIX
    }

    // 🔒 OPTIONAL: test a secured endpoint (if exists)
    @Test
    void testProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/secure")) // 🔁 change if you have secured endpoint
                .andExpect(status().isUnauthorized());
    }

}