package com.internship.tool.controller;

import com.internship.tool.config.JwtUtil;
import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;
import com.internship.tool.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // 🔥 NULL CHECK
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "User cannot be null"));
        }

        // 🔥 VALIDATION
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email is required"));
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password is required"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists"));
        }

        // 🔥 DEFAULT ROLE
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", savedUser.getId()
        ));
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and password required"));
        }

        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElse(null);

        if (dbUser == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "User not found"));
        }

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(dbUser);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", dbUser.getEmail(),
                "role", dbUser.getRole().name()
        ));
    }

    // =========================
    // REFRESH TOKEN
    // =========================
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = "Authorization", required = false) String header) {

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid Authorization header"));
        }

        try {
            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);

            User user = userRepository.findByEmail(username)
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "User not found"));
            }

            String newToken = jwtUtil.generateToken(user);

            return ResponseEntity.ok(Map.of("token", newToken));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid or expired token"));
        }
    }

    // =========================
    // GET ALL USERS (Protected)
    // =========================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userRepository.findAll();

        if (users == null || users.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(users);
    }

    // =========================
    // DELETE USER (Protected)
    // =========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (id == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID cannot be null"));
        }

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "User not found"));
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}