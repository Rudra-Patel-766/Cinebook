package com.moviebooking.model;

import java.sql.Date;
import java.sql.Time;

public class Show {
    private int    showId;
    private Date   showDate;
    private Time   showTime;
    private double ticketPrice;
    private int    movieId;
    private int    screenId;

    public Show() {}
    public Show(int showId, Date showDate, Time showTime, double ticketPrice, int movieId, int screenId) {
        this.showId      = showId;
        this.showDate    = showDate;
        this.showTime    = showTime;
        this.ticketPrice = ticketPrice;
        this.movieId     = movieId;
        this.screenId    = screenId;
    }

    public int    getShowId()      { return showId; }
    public Date   getShowDate()    { return showDate; }
    public Time   getShowTime()    { return showTime; }
    public double getTicketPrice() { return ticketPrice; }
    public int    getMovieId()     { return movieId; }
    public int    getScreenId()    { return screenId; }

    public void setShowId(int id)           { this.showId = id; }
    public void setShowDate(Date date)      { this.showDate = date; }
    public void setShowTime(Time time)      { this.showTime = time; }
    public void setTicketPrice(double p)    { this.ticketPrice = p; }
    public void setMovieId(int mid)         { this.movieId = mid; }
    public void setScreenId(int sid)        { this.screenId = sid; }

    @Override
    public String toString() {
        return String.format("ShowID: %d | Date: %s | Time: %s | Price: %.2f | MovieID: %d | ScreenID: %d",
                showId, showDate, showTime, ticketPrice, movieId, screenId);
    }
}
