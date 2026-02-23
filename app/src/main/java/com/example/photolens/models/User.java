package com.example.photolens.models;

public class User {
    private int id;
    private String email;
    private String passwordHash;
    private String role;
    private String teacherEmail;
    private String fullName;

    // ДОБАВЬ ЭТОТ БЛОК:
    public static class Role {
        public static final String STUDENT = "STUDENT";
        public static final String TEACHER = "TEACHER";
        public static final String ADMIN = "ADMIN";
    }

    public User() {}

    public User(int id, String email, String passwordHash, String role, String teacherEmail, String fullName) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.teacherEmail = teacherEmail;
        this.fullName = fullName;
    }

    public User(String email, String passwordHash, String role, String teacherEmail, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.teacherEmail = teacherEmail;
        this.fullName = fullName;
    }

    // Геттеры и сеттеры (оставь как были)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}