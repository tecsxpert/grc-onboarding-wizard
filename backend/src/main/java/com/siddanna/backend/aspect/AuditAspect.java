package com.siddanna.backend.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.siddanna.backend.model.AuditLog;
import com.siddanna.backend.repository.AuditLogRepository;

@Component
@Aspect
public class AuditAspect {

    private final AuditLogRepository repo;

    public AuditAspect(AuditLogRepository repo) {
        this.repo = repo;
    }

    // 🔥 CREATE
    @AfterReturning("execution(* com.siddanna.backend.controller.OnboardingController.save(..))")
    public void logCreate(JoinPoint joinPoint) {
        saveLog("CREATE", joinPoint);
    }

    // 🔥 UPDATE
    @AfterReturning("execution(* com.siddanna.backend.controller.OnboardingController.update(..))")
    public void logUpdate(JoinPoint joinPoint) {
        saveLog("UPDATE", joinPoint);
    }

    // 🔥 DELETE
    @AfterReturning("execution(* com.siddanna.backend.controller.OnboardingController.delete(..))")
    public void logDelete(JoinPoint joinPoint) {
        saveLog("DELETE", joinPoint);
    }

    // COMMON METHOD
    private void saveLog(String action, JoinPoint joinPoint) {

        AuditLog log = new AuditLog();

        log.setAction(action);
        log.setEntityName("Onboarding");
        log.setTimestamp(LocalDateTime.now());
        log.setUsername("admin"); // later JWT user

        // Try to get ID
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Long) {
            log.setEntityId((Long) args[0]);
        }

        repo.save(log);
    }
}