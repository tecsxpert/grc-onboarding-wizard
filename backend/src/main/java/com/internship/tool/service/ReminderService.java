package com.internship.tool.service;

import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    // Runs every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Kolkata")
    public void sendDailyReminders() {

        log.info("Starting daily reminder job...");

        List<User> users = userRepository.findAll();

        // ✅ handle null safely (coverage branch)
        if (users == null || users.isEmpty()) {
            log.warn("No users found for sending reminders.");
            return;
        }

        for (User user : users) {

            // ✅ skip null user (extra branch)
            if (user == null) {
                log.warn("Skipping null user");
                continue;
            }

            // ✅ skip invalid email
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                log.warn("Skipping user with missing email: {}", user.getId());
                continue;
            }

            try {
                Map<String, Object> data = new HashMap<>();
                data.put("name", user.getName());
                data.put("task", "Complete onboarding steps");
                data.put("deadline", "Today");

                emailService.sendEmail(
                        user.getEmail(),
                        "Daily Reminder",
                        "reminder",
                        data
                );

                log.info("Reminder sent to: {}", user.getEmail());

            } catch (Exception e) {
                // ✅ error branch (important for coverage)
                log.error("Failed to send email to: {}", user.getEmail(), e);
            }
        }

        log.info("Daily reminder job completed.");
    }
}