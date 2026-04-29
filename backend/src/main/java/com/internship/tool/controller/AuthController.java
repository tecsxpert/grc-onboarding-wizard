package com.internship.tool.controller;

import com.internship.tool.config.JwtUtil;
import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import com.internship.tool.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(dbUser);

        return Map.of("token", token);
    }

    // =========================
    // REFRESH TOKEN
    // =========================
    @GetMapping("/refresh")
    public Map<String, String> refresh(@RequestHeader("Authorization") String header) {

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = jwtUtil.generateToken(user);

        return Map.of("token", newToken);
    }

    // =========================
    // GET ALL USERS
    // =========================
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =========================
    // DELETE USER
    // =========================
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);

        return "User deleted successfully";
    }
}