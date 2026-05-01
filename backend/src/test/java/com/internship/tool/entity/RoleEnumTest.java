package com.internship.tool.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {

    @Test
    void testEnumValues() {
        Role role = Role.USER;

        assertEquals("USER", role.name());
        assertNotNull(Role.valueOf("USER"));
    }
}