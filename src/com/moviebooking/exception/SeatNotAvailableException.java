package com.moviebooking.exception;

public class SeatNotAvailableException extends MovieBookingException {
    public SeatNotAvailableException(String seatNumber) {
        super("Seat " + seatNumber + " is already booked or unavailable.");
    }
}
