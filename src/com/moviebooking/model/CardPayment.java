package com.moviebooking.model;

import java.sql.Date;

/**
 * CardPayment extends Payment – INHERITANCE.
 * Overrides getPaymentSummary() – POLYMORPHISM.
 */
public class CardPayment extends Payment {
    private String cardNumber; // store last 4 digits only
    private String expiryDate;

    public CardPayment() { super(); }
    public CardPayment(int paymentId, String paymentStatus, Date transactionDate,
                       int bookingId, String cardNumber, String expiryDate) {
        super(paymentId, "CARD", paymentStatus, transactionDate, bookingId);
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() { return cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public String getPaymentSummary() {
        return super.getPaymentSummary() +
               String.format(" | Card: **** **** **** %s | Expiry: %s", cardNumber, expiryDate);
    }
}
