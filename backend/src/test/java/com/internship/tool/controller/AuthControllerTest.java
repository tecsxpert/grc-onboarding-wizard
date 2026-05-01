package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    // =========================
    // REGISTER
    // =========================

    @Test
    @DisplayName("Register Success")
    void testRegisterSuccess() throws Exception {
        String json = """
        {
          "name": "Karthik",
          "email": "karthik@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    @DisplayName("Register Duplicate Email")
    void testRegisterDuplicate() throws Exception {
        String json = """
        {
          "name": "User",
          "email": "dup@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Register Missing Fields")
    void testRegisterMissingFields() throws Exception {
        String json = """
        {
          "email": "incomplete@gmail.com"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // =========================
    // LOGIN
    // =========================

    @Test
    @DisplayName("Login Success")
    void testLoginSuccess() throws Exception {

        String registerJson = """
        {
          "name": "User",
          "email": "login@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        String loginJson = """
        {
          "email": "login@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Login User Not Found")
    void testLoginUserNotFound() throws Exception {
        String json = """
        {
          "email": "wrong@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Login Wrong Password")
    void testLoginWrongPassword() throws Exception {

        String registerJson = """
        {
          "name": "User",
          "email": "edge@gmail.com",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        String wrongLogin = """
        {
          "email": "edge@gmail.com",
          "password": "wrong"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongLogin))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Login Null Payload")
    void testLoginNullPayload() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    // =========================
    // REFRESH TOKEN
    // =========================

    @Test
    @DisplayName("Refresh Invalid Header")
    void testRefreshInvalidHeader() throws Exception {
        mockMvc.perform(get("/api/auth/refresh"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Refresh Malformed Token")
    void testRefreshMalformedToken() throws Exception {
        mockMvc.perform(get("/api/auth/refresh")
                        .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }

    // =========================
    // USERS
    // =========================

    @Test
    @DisplayName("Get Users")
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // =========================
    // DELETE USER
    // =========================

    @Test
    @DisplayName("Delete User Not Found")
    void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/auth/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete User Success")
    void testDeleteUserSuccess() throws Exception {

        String json = """
        {
          "name": "User",
          "email": "delete@gmail.com",
          "password": "1234"
        }
        """;

        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // ✅ BEST FIX (no regex issues)
        Map<String, Object> map = mapper.readValue(response, Map.class);
        Integer id = (Integer) map.get("userId");

        mockMvc.perform(delete("/api/auth/delete/" + id))
                .andExpect(status().isOk());
    }
    @Test
    void testRegisterInvalidEmailFormat() throws Exception {
        String json = """
    {
      "name": "Test",
      "email": "invalid-email",
      "password": "1234"
    }
    """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testRefreshWithoutHeader() throws Exception {
        mockMvc.perform(get("/api/auth/refresh"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void loginWrongPassword() throws Exception {
        String json = """
    {
      "email": "test@gmail.com",
      "password": "wrong"
    }
    """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }
}