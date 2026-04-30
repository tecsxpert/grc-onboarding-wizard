package com.siddanna.backend.controller;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.siddanna.backend.model.Onboarding;
import com.siddanna.backend.repository.OnboardingRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

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
    public Onboarding save(@Valid @RequestBody Onboarding data) {
        return repo.save(data);
    }

    // ✅ READ (Pagination + Date Filter + Soft Delete)
    @GetMapping
    public Page<Onboarding> getAll(
            Pageable pageable,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() && !endDate.isEmpty()) {

            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59);

            return repo.findByDeletedFalseAndCreatedAtBetween(start, end, pageable);
        }

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
    public List<Onboarding> search(@RequestParam String q) {
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

    // 🔥 CSV EXPORT
    @GetMapping("/export")
    public void exportCsv(HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=onboarding.csv");

        List<Onboarding> list = repo.findAll();
        PrintWriter writer = response.getWriter();

        // header
        writer.println("ID,Name,Email,Role");

        // data
        for (Onboarding o : list) {
            writer.println(
                    o.getId() + "," +
                    o.getName() + "," +
                    o.getEmail() + "," +
                    o.getRole()
            );
        }

        writer.flush();
        writer.close();
    }

    // 🔥 FILE UPLOAD (FINAL FIXED VERSION)
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        // ❌ empty
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // ❌ size > 2MB
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new RuntimeException("File too large (max 2MB)");
        }

        // ❌ type check
        String type = file.getContentType();
        if (type == null || 
            (!type.equals("image/png") && !type.equals("image/jpeg"))) {
            throw new RuntimeException("Only PNG/JPG allowed");
        }

        return "File uploaded successfully: " + file.getOriginalFilename();
    }
}