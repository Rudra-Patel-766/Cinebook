package com.moviebooking.model;

public class Screen {
    private int screenId;
    private int screenNumber;
    private int theatreId;

    public Screen() {}
    public Screen(int screenId, int screenNumber, int theatreId) {
        this.screenId     = screenId;
        this.screenNumber = screenNumber;
        this.theatreId    = theatreId;
    }

    public int getScreenId()     { return screenId; }
    public int getScreenNumber() { return screenNumber; }
    public int getTheatreId()    { return theatreId; }

    public void setScreenId(int id)       { this.screenId = id; }
    public void setScreenNumber(int num)  { this.screenNumber = num; }
    public void setTheatreId(int tid)     { this.theatreId = tid; }

    @Override
    public String toString() {
        return String.format("ScreenID: %d | Screen No: %d | TheatreID: %d", screenId, screenNumber, theatreId);
    }
}
