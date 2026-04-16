package com.moviebooking.model;

/**
 * Customer extends User – INHERITANCE.
 * Overrides getDetails() – POLYMORPHISM (Method Overriding).
 */
public class Customer extends User {

    private int    loyaltyPoints;
    private String preferredLanguage;
    private String accountStatus;   // "ACTIVE", "SUSPENDED"

    public Customer() { super(); }

    public Customer(int userId, String name, String email, String phone,
                    String password, int loyaltyPoints, String preferredLanguage, String accountStatus) {
        super(userId, name, email, phone, password, "CUSTOMER");
        this.loyaltyPoints     = loyaltyPoints;
        this.preferredLanguage = preferredLanguage;
        this.accountStatus     = accountStatus;
    }

    public int    getLoyaltyPoints()     { return loyaltyPoints; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public String getAccountStatus()     { return accountStatus; }

    public void setLoyaltyPoints(int pts)          { this.loyaltyPoints = pts; }
    public void setPreferredLanguage(String lang)  { this.preferredLanguage = lang; }
    public void setAccountStatus(String status)    { this.accountStatus = status; }

    // Method Overriding – POLYMORPHISM
    @Override
    public String getDetails() {
        return super.getDetails() +
               String.format(" | LoyaltyPts: %d | Language: %s | Status: %s",
                       loyaltyPoints, preferredLanguage, accountStatus);
    }
}
