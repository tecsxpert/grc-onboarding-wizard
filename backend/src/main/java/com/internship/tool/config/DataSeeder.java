package com.internship.tool.config;

import com.internship.tool.entity.User;
import com.internship.tool.entity.Role;   // ✅ ADD THIS
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) return;

        List<User> users = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            User user = new User();

            user.setName("User " + i);
            user.setEmail("user" + i + "@test.com");
            user.setPassword("password123");

            // ✅ FIXED
            user.setRole(Role.USER);

            users.add(user);
        }

        userRepository.saveAll(users);

        System.out.println("✅ Seeded 30 users");
    }
}