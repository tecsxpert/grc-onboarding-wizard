package com.siddanna.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siddanna.backend.model.Onboarding;
import com.siddanna.backend.repository.OnboardingRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(OnboardingRepository repo) {
        return args -> {

            // avoid duplicate seeding
            if (repo.count() > 0) return;

            String[] names = {
                "Rahul Sharma","Priya Verma","Amit Singh","Neha Gupta","Rohan Das",
                "Sneha Iyer","Karan Mehta","Anjali Nair","Vikram Rao","Pooja Jain",
                "Arjun Reddy","Meera Kapoor","Siddharth Roy","Kavya Pillai","Manish Yadav"
            };

            String[] roles = {
                "Backend Developer","Frontend Developer","QA Engineer","DevOps Engineer",
                "Data Analyst","Full Stack Developer","UI/UX Designer","Cloud Engineer",
                "Security Analyst","ML Engineer","Support Engineer","Mobile Developer",
                "System Admin","Product Analyst","Software Engineer"
            };

            for (int i = 0; i < 15; i++) {
                Onboarding o = new Onboarding();
                o.setName(names[i]);
                o.setEmail("user" + i + "@gmail.com");
                o.setRole(roles[i]);
                o.setDescription("Demo candidate " + (i + 1));
                o.setDeleted(false);

                repo.save(o);
            }

            System.out.println("✅ 15 demo records inserted");
        };
    }
}