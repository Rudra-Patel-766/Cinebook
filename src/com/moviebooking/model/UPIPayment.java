package com.moviebooking.model;

import java.sql.Date;

/**
 * UPIPayment extends Payment – INHERITANCE.
 * Overrides getPaymentSummary() – POLYMORPHISM.
 */
public class UPIPayment extends Payment {
    private String upiId;

    public UPIPayment() { super(); }
    public UPIPayment(int paymentId, String paymentStatus, Date transactionDate,
                      int bookingId, String upiId) {
        super(paymentId, "UPI", paymentStatus, transactionDate, bookingId);
        this.upiId = upiId;
    }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    @Override
    public String getPaymentSummary() {
        return super.getPaymentSummary() + " | UPI ID: " + upiId;
    }
}
