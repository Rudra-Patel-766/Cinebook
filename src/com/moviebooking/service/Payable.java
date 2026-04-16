package com.moviebooking.service;

import com.moviebooking.exception.InvalidPaymentException;

/**
 * Interface defining the contract for any payment method.
 */
public interface Payable {
    boolean processPayment(int bookingId, double amount) throws InvalidPaymentException;
    String getPaymentMode();
}
