package com.siddanna.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siddanna.backend.model.Onboarding;
import com.siddanna.backend.repository.OnboardingRepository;

@RestController
@RequestMapping("/onboarding")
@CrossOrigin(origins = "http://localhost:5173")
public class OnboardingController {

    private final OnboardingRepository repo;

    public OnboardingController(OnboardingRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE
    @PostMapping
    public Onboarding save(@RequestBody Onboarding data) {
        return repo.save(data);
    }

    // ✅ READ (Pagination + Date Filter + Soft Delete)
    @GetMapping
    public Page<Onboarding> getAll(
            Pageable pageable,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        // 🔥 DATE FILTER LOGIC
        if (startDate != null && endDate != null &&
            !startDate.isEmpty() && !endDate.isEmpty()) {

            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59);

            return repo.findByDeletedFalseAndCreatedAtBetween(start, end, pageable);
        }

        // DEFAULT
        return repo.findByDeletedFalse(pageable);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Onboarding> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Onboarding update(@PathVariable Long id, @RequestBody Onboarding data) {
        Onboarding existing = repo.findById(id).orElseThrow();

        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setRole(data.getRole());
        existing.setDescription(data.getDescription());

        return repo.save(existing);
    }

    // ✅ DELETE (Soft Delete)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Onboarding obj = repo.findById(id).orElseThrow();
        obj.setDeleted(true);
        repo.save(obj);
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public java.util.List<Onboarding> search(@RequestParam String q) {
        return repo.findAll().stream()
                .filter(o -> !Boolean.TRUE.equals(o.getDeleted()))
                .filter(o -> o.getName().toLowerCase().contains(q.toLowerCase()))
                .toList();
    }

    // ✅ STATS API
    @GetMapping("/stats")
    public Map<String, Long> getStats() {

        long total = repo.count();
        long active = repo.countByDeletedFalse();
        long deleted = repo.countByDeletedTrue();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("active", active);
        stats.put("deleted", deleted);

        return stats;
    }
}