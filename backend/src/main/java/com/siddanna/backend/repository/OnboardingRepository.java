package com.siddanna.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siddanna.backend.model.Onboarding;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
}