package com.moviebooking.service;

import com.moviebooking.exception.MovieBookingException;

/**
 * Interface defining the contract for any bookable entity.
 * Demonstrates INTERFACE usage as required by OOPs rubric.
 */
public interface Bookable {
    boolean book(int userId, int showId, int seatId) throws MovieBookingException;
    boolean cancel(int bookingId) throws MovieBookingException;
    void viewBookingDetails(int bookingId) throws MovieBookingException;
}
