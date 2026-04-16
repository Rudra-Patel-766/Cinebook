package com.moviebooking.exception;

public class UserNotFoundException extends MovieBookingException {
    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
