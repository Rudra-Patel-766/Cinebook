package com.moviebooking.model;

/**
 * Admin extends User – INHERITANCE.
 * Overrides getDetails() – POLYMORPHISM (Method Overriding).
 */
public class Admin extends User {

    private String accessLevel;  // e.g., "SUPER_ADMIN", "MANAGER"
    private String adminStatus;  // e.g., "ACTIVE", "INACTIVE"

    public Admin() { super(); }

    public Admin(int userId, String name, String email, String phone,
                 String password, String accessLevel, String adminStatus) {
        super(userId, name, email, phone, password, "ADMIN");
        this.accessLevel = accessLevel;
        this.adminStatus = adminStatus;
    }

    public String getAccessLevel() { return accessLevel; }
    public String getAdminStatus() { return adminStatus; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public void setAdminStatus(String adminStatus) { this.adminStatus = adminStatus; }

    // Method Overriding – POLYMORPHISM
    @Override
    public String getDetails() {
        return super.getDetails() +
               String.format(" | AccessLevel: %s | Status: %s", accessLevel, adminStatus);
    }
}
