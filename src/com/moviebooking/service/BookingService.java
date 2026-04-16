package com.moviebooking.service;

import com.moviebooking.dao.*;
import com.moviebooking.exception.*;
import com.moviebooking.model.*;

import java.sql.Date;

/**
 * BookingService implements Bookable – demonstrates INTERFACE usage.
 * Orchestrates Booking + Payment + Invoice + Cancellation + Refund flow.
 */
public class BookingService implements Bookable {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CancellationDAO cancellationDAO = new CancellationDAO();

    // ── book() – implements Bookable ──────────────────────────────────────
    @Override
    public boolean book(int userId, int showId, int seatId) throws MovieBookingException {
        try {
            Date today = new Date(System.currentTimeMillis());
            Booking booking = new Booking(0, today, "CONFIRMED", 0.0, userId, showId, seatId);
            int bookingId = bookingDAO.addBooking(booking);
            if (bookingId == -1)
                throw new MovieBookingException("Failed to create booking.");
            System.out.println("[Booking] BookingID " + bookingId + " confirmed for UserID " + userId);
            return true;
        } catch (SeatNotAvailableException e) {
            throw e;
        } catch (DatabaseConnectionException e) {
            throw new MovieBookingException("DB error during booking: " + e.getMessage(), e);
        }
    }

    // ── processPayment() ─────────────────────────────────────────────────
    public boolean processPayment(int bookingId, double amount, String mode, String extraInfo)
            throws MovieBookingException {
        try {
            Date today = new Date(System.currentTimeMillis());
            Payment payment = new Payment(0, mode, "SUCCESS", today, bookingId);
            int paymentId = paymentDAO.addPayment(payment);
            if (paymentId == -1)
                throw new InvalidPaymentException("Payment processing failed.");

            // Generate invoice with 18% GST
            double tax = amount * 0.18;
            DigitalInvoice invoice = new DigitalInvoice(0, today, tax, paymentId);
            paymentDAO.generateInvoice(invoice);

            // Print invoice
            System.out.println("\n========== DIGITAL INVOICE ==========");
            System.out.println("BookingID   : " + bookingId);
            System.out.println("PaymentID   : " + paymentId);
            System.out.println("Mode        : " + mode);
            System.out.printf("Amount      : Rs. %.2f%n", amount);
            System.out.printf("GST (18%%)   : Rs. %.2f%n", tax);
            System.out.printf("Total       : Rs. %.2f%n", amount + tax);
            System.out.println("Status      : SUCCESS");
            System.out.println("=====================================\n");
            return true;

        } catch (InvalidPaymentException e) {
            throw e;
        } catch (DatabaseConnectionException e) {
            throw new MovieBookingException("DB error during payment: " + e.getMessage(), e);
        }
    }

    // ── cancel() – implements Bookable ────────────────────────────────────
    @Override
    public boolean cancel(int bookingId) throws MovieBookingException {
        try {
            Date today = new Date(System.currentTimeMillis());

            // 1. Update booking status
            bookingDAO.updateBookingStatus(bookingId, "CANCELLED");

            // 2. Add cancellation record
            Cancellation c = new Cancellation(0, "Customer requested", today, bookingId);
            int cancelId = cancellationDAO.addCancellation(c);

            // 3. Get payment and create refund
            Payment payment = paymentDAO.getPaymentByBooking(bookingId);
            if (payment != null) {
                paymentDAO.updatePaymentStatus(payment.getPaymentId(), "REFUND_INITIATED");
                Refund refund = new Refund(0, "PENDING", 0.0, payment.getPaymentId(), cancelId);
                cancellationDAO.addRefund(refund);
                System.out.println("[OK] Cancellation done. Refund initiated for PaymentID: " + payment.getPaymentId());
            }
            return true;

        } catch (DatabaseConnectionException e) {
            throw new MovieBookingException("DB error during cancellation: " + e.getMessage(), e);
        }
    }

    // ── viewBookingDetails() – implements Bookable ────────────────────────
    @Override
    public void viewBookingDetails(int bookingId) throws MovieBookingException {
        try {
            Booking b = bookingDAO.getBookingById(bookingId);
            if (b == null)
                throw new MovieBookingException("Booking not found: ID " + bookingId);

            System.out.println("\n======= BOOKING DETAILS =======");
            System.out.println(b);

            Payment p = paymentDAO.getPaymentByBooking(bookingId);
            if (p != null) {
                System.out.println("Payment     : " + p.getPaymentSummary());
                DigitalInvoice inv = paymentDAO.getInvoiceByPayment(p.getPaymentId());
                if (inv != null)
                    System.out.println("Invoice     : " + inv);
            }
            System.out.println("================================\n");

        } catch (DatabaseConnectionException e) {
            throw new MovieBookingException("DB error viewing booking: " + e.getMessage(), e);
        }
    }
}
