package com.moviebooking.model;

public class Seat {
    private int    seatId;
    private String seatNumber;
    private String seatType;
    private int    screenId;

    public Seat() {}
    public Seat(int seatId, String seatNumber, String seatType, int screenId) {
        this.seatId     = seatId;
        this.seatNumber = seatNumber;
        this.seatType   = seatType;
        this.screenId   = screenId;
    }

    public int    getSeatId()     { return seatId; }
    public String getSeatNumber() { return seatNumber; }
    public String getSeatType()   { return seatType; }
    public int    getScreenId()   { return screenId; }

    public void setSeatId(int id)          { this.seatId = id; }
    public void setSeatNumber(String num)  { this.seatNumber = num; }
    public void setSeatType(String type)   { this.seatType = type; }
    public void setScreenId(int sid)       { this.screenId = sid; }

    @Override
    public String toString() {
        return String.format("SeatID: %d | Seat: %-5s | Type: %-10s | ScreenID: %d",
                seatId, seatNumber, seatType, screenId);
    }
}
