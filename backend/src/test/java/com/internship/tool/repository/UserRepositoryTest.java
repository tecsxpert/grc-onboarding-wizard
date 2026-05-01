package com.internship.tool.repository;

import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // =========================
    // SAVE USER
    // =========================
    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Karthik");
        user.setEmail("abc@gmail.com");
        user.setPassword("123");
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("abc@gmail.com", saved.getEmail());
    }

    // =========================
    // FIND BY EMAIL
    // =========================
    @Test
    void testFindByEmail() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("find@test.com");
        user.setPassword("123");
        user.setRole(Role.USER);

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("find@test.com");

        assertTrue(found.isPresent());
        assertEquals("find@test.com", found.get().getEmail());
    }

    // =========================
    // EXISTS BY EMAIL
    // =========================
    @Test
    void testExistsByEmail() {
        User user = new User();
        user.setName("Check User");
        user.setEmail("exists@test.com");
        user.setPassword("123");
        user.setRole(Role.USER);

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("exists@test.com");

        assertTrue(exists);
    }

    // =========================
    // FIND BY ID
    // =========================
    @Test
    void testFindById() {
        User user = new User();
        user.setName("ID User");
        user.setEmail("id@test.com");
        user.setPassword("123");
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("id@test.com", found.get().getEmail());
    }

    // =========================
    // DELETE USER
    // =========================
    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("Delete User");
        user.setEmail("delete@test.com");
        user.setPassword("123");
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());

        Optional<User> deleted = userRepository.findById(saved.getId());

        assertFalse(deleted.isPresent());
    }

    // =========================
    // FIND ALL USERS
    // =========================
    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("u1@test.com");
        user1.setPassword("123");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("u2@test.com");
        user2.setPassword("123");
        user2.setRole(Role.USER);

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertTrue(users.size() >= 2);
    }
}