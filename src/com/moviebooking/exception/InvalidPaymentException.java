package com.moviebooking.exception;

public class InvalidPaymentException extends MovieBookingException {
    public InvalidPaymentException(String message) {
        super("Payment Error: " + message);
    }
}
