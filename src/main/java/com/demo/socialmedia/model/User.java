package com.demo.socialmedia.model;

import java.sql.Timestamp;

/**
 * Model đại diện cho bảng users trong CSDL.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private Timestamp createdAt;

    // Constructor mặc định
    public User() {}

    // Constructor đầy đủ
    public User(int id, String username, String password, String role, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
