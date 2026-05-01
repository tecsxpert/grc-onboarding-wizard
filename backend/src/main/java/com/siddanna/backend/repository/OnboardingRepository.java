package com.siddanna.backend.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.siddanna.backend.model.Onboarding;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {

    // ✅ Existing
    Page<Onboarding> findByDeletedFalse(Pageable pageable);

    long countByDeletedFalse();
    long countByDeletedTrue();

    // 🔥 NEW (DAY 7 - DATE FILTER)
    Page<Onboarding> findByDeletedFalseAndCreatedAtBetween(
        LocalDateTime start,
        LocalDateTime end,
        Pageable pageable
    );
}