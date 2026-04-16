package com.moviebooking.model;

public class Movie {
    private int    movieId;
    private String title;
    private String genre;
    private int    duration;   // in minutes
    private String language;

    public Movie() {}

    public Movie(int movieId, String title, String genre, int duration, String language) {
        this.movieId  = movieId;
        this.title    = title;
        this.genre    = genre;
        this.duration = duration;
        this.language = language;
    }

    public int    getMovieId()  { return movieId; }
    public String getTitle()    { return title; }
    public String getGenre()    { return genre; }
    public int    getDuration() { return duration; }
    public String getLanguage() { return language; }

    public void setMovieId(int id)       { this.movieId = id; }
    public void setTitle(String title)   { this.title = title; }
    public void setGenre(String genre)   { this.genre = genre; }
    public void setDuration(int dur)     { this.duration = dur; }
    public void setLanguage(String lang) { this.language = lang; }

    @Override
    public String toString() {
        return String.format("MovieID: %d | Title: %-25s | Genre: %-10s | Duration: %d min | Language: %s",
                movieId, title, genre, duration, language);
    }
}
