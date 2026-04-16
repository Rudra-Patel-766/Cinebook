package com.moviebooking.model;

import com.moviebooking.util.ValidationUtil;

public class User {

    private int userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    public User() {}

    public User(int userId, String name, String email, String phone, String password, String role) {
        this.userId = userId;
        this.name = name;
        setEmail(email);
        setPhone(phone);
        this.password = password;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }

    public void setEmail(String email) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Phone must be exactly 10 digits");
        }
        this.phone = phone;
    }

    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    public String getDetails() {
        return String.format("UserID: %d | Name: %s | Email: %s | Phone: %s | Role: %s",
                userId, name, email, phone, role);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}