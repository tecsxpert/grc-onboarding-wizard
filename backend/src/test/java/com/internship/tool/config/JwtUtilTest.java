package com.internship.tool.config;

import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    // =========================
    // SUCCESS FLOW
    // =========================
    @Test
    void testGenerateAndValidateToken() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);

        String token = jwtUtil.generateToken(user);

        assertNotNull(token);

        String username = jwtUtil.extractUsername(token);
        assertEquals("test@gmail.com", username);

        String role = jwtUtil.extractRole(token);
        assertEquals("USER", role);

        boolean isValid = jwtUtil.validateToken(token, "test@gmail.com");
        assertTrue(isValid);
    }

    // =========================
    // INVALID TOKEN
    // =========================
    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });
    }

    // =========================
    // INVALID USER VALIDATION
    // =========================
    @Test
    void validateToken_invalidUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);

        String token = jwtUtil.generateToken(user);

        boolean result = jwtUtil.validateToken(token, "wrong@gmail.com");

        assertFalse(result);
    }

    // =========================
    // EXTRACT INVALID TOKEN
    // =========================
    @Test
    void extractUsername_invalidToken() {
        assertThrows(Exception.class,
                () -> jwtUtil.extractUsername("invalid.token"));
    }

    // =========================
    // 🔥 NEW TESTS (COVERAGE BOOST)
    // =========================

    @Test
    void extractRole_invalidToken() {
        assertThrows(Exception.class,
                () -> jwtUtil.extractRole("invalid.token"));
    }

    @Test
    void validateToken_expiredOrWrongUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);

        String token = jwtUtil.generateToken(user);

        // wrong username → should fail
        boolean result = jwtUtil.validateToken(token, "wrong@gmail.com");

        assertFalse(result);
    }
}