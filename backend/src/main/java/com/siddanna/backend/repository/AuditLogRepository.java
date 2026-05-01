package com.siddanna.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siddanna.backend.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}