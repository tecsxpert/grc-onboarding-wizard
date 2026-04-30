package com.internship.tool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // NAME
    // =========================
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    // =========================
    // EMAIL
    // =========================
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    // =========================
    // PASSWORD (WRITE ONLY)
    // =========================
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    // =========================
    // ROLE
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // =========================
    // CONSTRUCTORS
    // =========================
    public User() {}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================
    public Long getId() {
        return id;
    }

    // ⚠️ optional (can remove if not needed)
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // ⚠️ important for encoding
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}