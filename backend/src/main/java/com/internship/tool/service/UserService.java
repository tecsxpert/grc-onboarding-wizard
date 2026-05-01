package com.internship.tool.service;

import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import com.internship.tool.repository.UserRepository;
import com.internship.tool.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Collections;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // CREATE USER
    // =========================
    @CacheEvict(value = {"users", "user", "userEmail", "usersPage"}, allEntries = true)
    public User createUser(User user) {

        validateUser(user);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // 🔥 Branch: default role
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // =========================
    // GET ALL USERS
    // =========================
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        logger.info("Fetching users from DB...");

        List<User> users = userRepository.findAll();

        // 🔥 Branch for null safety (important for coverage)
        if (users == null) {
            return Collections.emptyList();
        }

        return users;
    }

    // =========================
    // GET ALL USERS (PAGINATED)
    // =========================
    @Cacheable(value = "usersPage",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<User> getAllUsers(Pageable pageable) {

        if (pageable == null) {
            throw new InvalidInputException("Pageable cannot be null"); // 🔥 new branch
        }

        logger.info("Fetching paginated users from DB...");
        return userRepository.findAll(pageable);
    }

    // =========================
    // GET USER BY ID
    // =========================
    @Cacheable(value = "user", key = "#id")
    public User getUserById(Long id) {

        if (id == null) {
            throw new InvalidInputException("ID cannot be null"); // 🔥 new branch
        }

        logger.info("Fetching user by ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));
    }

    // =========================
    // GET USER BY EMAIL
    // =========================
    @Cacheable(value = "userEmail", key = "#email")
    public User getUserByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        logger.info("Fetching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));
    }

    // =========================
    // UPDATE USER
    // =========================
    @CacheEvict(value = {"users", "user", "userEmail", "usersPage"}, allEntries = true)
    public User updateUser(Long id, User user) {

        if (user == null) {
            throw new InvalidInputException("User cannot be null"); // 🔥 new branch
        }

        User existingUser = getUserById(id);

        // Name update
        if (user.getName() != null && !user.getName().isBlank()) {
            existingUser.setName(user.getName());
        }

        // Email update
        if (user.getEmail() != null && !user.getEmail().isBlank()) {

            if (!existingUser.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmail(user.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }

            existingUser.setEmail(user.getEmail());
        }

        // Password update
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Role update
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        return userRepository.save(existingUser);
    }

    // =========================
    // DELETE USER
    // =========================
    @CacheEvict(value = {"users", "user", "userEmail", "usersPage"}, allEntries = true)
    public void deleteUser(Long id) {

        if (id == null) {
            throw new InvalidInputException("ID cannot be null"); // 🔥 new branch
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);
    }

    // =========================
    // VALIDATION
    // =========================
    private void validateUser(User user) {

        if (user == null) {
            throw new InvalidInputException("User cannot be null");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidInputException("Name is required");
        }

        if (user.getEmail() == null ||
                !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new InvalidInputException("Password is required");
        }
    }
}