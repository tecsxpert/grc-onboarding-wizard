package com.internship.tool.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigExtraTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void validateToken_nullToken() {
        assertThrows(Exception.class,
                () -> jwtUtil.extractUsername(null));
    }

    @Test
    void validateToken_emptyToken() {
        assertThrows(Exception.class,
                () -> jwtUtil.extractUsername(""));
    }

    @Test
    void extractRole_nullToken() {
        assertThrows(Exception.class,
                () -> jwtUtil.extractRole(null));

    }

}