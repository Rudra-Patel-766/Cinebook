package com.moviebooking.exception;

// ─────────────────────────────────────────────
// 1. Base custom exception
// ─────────────────────────────────────────────
public class MovieBookingException extends Exception {
    public MovieBookingException(String message) {
        super(message);
    }
    public MovieBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
