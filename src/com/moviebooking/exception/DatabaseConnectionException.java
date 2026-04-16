package com.moviebooking.exception;

public class DatabaseConnectionException extends MovieBookingException {
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
