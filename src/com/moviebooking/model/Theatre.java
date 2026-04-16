package com.moviebooking.model;

public class Theatre {
    private int    theatreId;
    private String name;
    private String location;

    public Theatre() {}
    public Theatre(int theatreId, String name, String location) {
        this.theatreId = theatreId;
        this.name      = name;
        this.location  = location;
    }

    public int    getTheatreId() { return theatreId; }
    public String getName()      { return name; }
    public String getLocation()  { return location; }

    public void setTheatreId(int id)    { this.theatreId = id; }
    public void setName(String name)    { this.name = name; }
    public void setLocation(String loc) { this.location = loc; }

    @Override
    public String toString() {
        return String.format("TheatreID: %d | Name: %-20s | Location: %s", theatreId, name, location);
    }
}
