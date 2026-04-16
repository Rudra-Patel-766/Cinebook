package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public boolean addMovie(Movie movie) throws DatabaseConnectionException {
        String sql = "INSERT INTO Movie (Title, Genre, Duration, Language) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setString(4, movie.getLanguage());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Movie added: " + movie.getTitle()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addMovie: " + e.getMessage());
        }
        return false;
    }

    public boolean updateMovie(Movie movie) throws DatabaseConnectionException {
        String sql = "UPDATE Movie SET Title=?, Genre=?, Duration=?, Language=? WHERE MovieID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setString(4, movie.getLanguage());
            ps.setInt(5, movie.getMovieId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Movie updated: ID " + movie.getMovieId()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateMovie: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteMovie(int movieId) throws DatabaseConnectionException {
        String sql = "DELETE FROM Movie WHERE MovieID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Movie deleted: ID " + movieId); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] deleteMovie: " + e.getMessage());
        }
        return false;
    }

    public List<Movie> getAllMovies() throws DatabaseConnectionException {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM Movie";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapMovie(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllMovies: " + e.getMessage());
        }
        return list;
    }

    public Movie getMovieById(int id) throws DatabaseConnectionException {
        String sql = "SELECT * FROM Movie WHERE MovieID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapMovie(rs);
        } catch (SQLException e) {
            System.err.println("[SQL Error] getMovieById: " + e.getMessage());
        }
        return null;
    }

    public List<Movie> getMoviesByGenre(String genre) throws DatabaseConnectionException {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM Movie WHERE Genre=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapMovie(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getMoviesByGenre: " + e.getMessage());
        }
        return list;
    }

    private Movie mapMovie(ResultSet rs) throws SQLException {
        return new Movie(rs.getInt("MovieID"), rs.getString("Title"),
                rs.getString("Genre"), rs.getInt("Duration"), rs.getString("Language"));
    }
}