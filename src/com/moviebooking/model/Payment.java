package com.moviebooking.model;

import java.sql.Date;

/**
 * Parent Payment class.
 * Card, UPI, NetBanking EXTEND this – INHERITANCE.
 */
public class Payment {
    private int    paymentId;
    private String mode;          // "CARD", "UPI", "NETBANKING"
    private String paymentStatus; // "SUCCESS", "FAILED", "PENDING"
    private Date   transactionDate;
    private int    bookingId;

    public Payment() {}
    public Payment(int paymentId, String mode, String paymentStatus, Date transactionDate, int bookingId) {
        this.paymentId       = paymentId;
        this.mode            = mode;
        this.paymentStatus   = paymentStatus;
        this.transactionDate = transactionDate;
        this.bookingId       = bookingId;
    }

    public int    getPaymentId()       { return paymentId; }
    public String getMode()            { return mode; }
    public String getPaymentStatus()   { return paymentStatus; }
    public Date   getTransactionDate() { return transactionDate; }
    public int    getBookingId()       { return bookingId; }

    public void setPaymentId(int id)            { this.paymentId = id; }
    public void setMode(String mode)            { this.mode = mode; }
    public void setPaymentStatus(String status) { this.paymentStatus = status; }
    public void setTransactionDate(Date date)   { this.transactionDate = date; }
    public void setBookingId(int bid)           { this.bookingId = bid; }

    // Overridden in subclasses – POLYMORPHISM
    public String getPaymentSummary() {
        return String.format("PaymentID: %d | Mode: %s | Status: %s | Date: %s | BookingID: %d",
                paymentId, mode, paymentStatus, transactionDate, bookingId);
    }

    @Override
    public String toString() { return getPaymentSummary(); }
}
