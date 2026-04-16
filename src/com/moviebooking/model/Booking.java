package com.moviebooking.model;

import java.sql.Date;

public class Booking {
    private int    bookingId;
    private Date   bookingDate;
    private String status;       // "CONFIRMED", "CANCELLED", "PENDING"
    private double totalAmount;
    private int    userId;
    private int    showId;
    private int    seatId;

    public Booking() {}
    public Booking(int bookingId, Date bookingDate, String status,
                   double totalAmount, int userId, int showId, int seatId) {
        this.bookingId   = bookingId;
        this.bookingDate = bookingDate;
        this.status      = status;
        this.totalAmount = totalAmount;
        this.userId      = userId;
        this.showId      = showId;
        this.seatId      = seatId;
    }

    public int    getBookingId()   { return bookingId; }
    public Date   getBookingDate() { return bookingDate; }
    public String getStatus()      { return status; }
    public double getTotalAmount() { return totalAmount; }
    public int    getUserId()      { return userId; }
    public int    getShowId()      { return showId; }
    public int    getSeatId()      { return seatId; }

    public void setBookingId(int id)        { this.bookingId = id; }
    public void setBookingDate(Date date)   { this.bookingDate = date; }
    public void setStatus(String status)    { this.status = status; }
    public void setTotalAmount(double amt)  { this.totalAmount = amt; }
    public void setUserId(int uid)          { this.userId = uid; }
    public void setShowId(int sid)          { this.showId = sid; }
    public void setSeatId(int sid)          { this.seatId = sid; }

    @Override
    public String toString() {
        return String.format("BookingID: %d | Date: %s | Status: %-10s | Amount: %.2f | UserID: %d | ShowID: %d",
                bookingId, bookingDate, status, totalAmount, userId, showId);
    }
}
