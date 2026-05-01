package com.internship.tool.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersSetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Karthik");
        user.setEmail("test@gmail.com");
        user.setPassword("1234");
        user.setRole(Role.USER);

        assertEquals(1L, user.getId());
        assertEquals("Karthik", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }
}