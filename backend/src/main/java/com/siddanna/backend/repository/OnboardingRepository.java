package com.siddanna.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.siddanna.backend.model.Onboarding;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {

    // ✅ ADD THIS LINE (this is missing in your code)
    Page<Onboarding> findByDeletedFalse(Pageable pageable);
}