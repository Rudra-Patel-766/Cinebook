package com.moviebooking.model;

import java.sql.Date;

// ─── Cancellation ──────────────────────────────────────────────────────────
public class Cancellation {
    private int    cancelId;
    private String reason;
    private Date   cancellationDate;
    private int    bookingId;

    public Cancellation() {}
    public Cancellation(int cancelId, String reason, Date cancellationDate, int bookingId) {
        this.cancelId         = cancelId;
        this.reason           = reason;
        this.cancellationDate = cancellationDate;
        this.bookingId        = bookingId;
    }

    public int    getCancelId()         { return cancelId; }
    public String getReason()           { return reason; }
    public Date   getCancellationDate() { return cancellationDate; }
    public int    getBookingId()        { return bookingId; }

    public void setCancelId(int id)            { this.cancelId = id; }
    public void setReason(String reason)       { this.reason = reason; }
    public void setCancellationDate(Date date) { this.cancellationDate = date; }
    public void setBookingId(int bid)          { this.bookingId = bid; }

    @Override
    public String toString() {
        return String.format("CancelID: %d | Reason: %-20s | Date: %s | BookingID: %d",
                cancelId, reason, cancellationDate, bookingId);
    }
}
