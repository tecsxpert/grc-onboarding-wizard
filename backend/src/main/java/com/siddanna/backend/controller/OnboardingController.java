package com.siddanna.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    // ✅ READ (Pagination + Soft Delete)
    @GetMapping
    public Page<Onboarding> getAll(Pageable pageable) {
        return repo.findByDeletedFalse(pageable);
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
}