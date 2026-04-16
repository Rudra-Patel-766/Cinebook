package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.model.Show;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    public boolean addShow(Show show) throws DatabaseConnectionException {
        String sql = "INSERT INTO Shows (ShowDate, ShowTime, TicketPrice, MovieID, ScreenID) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, show.getShowDate());
            ps.setTime(2, show.getShowTime());
            ps.setDouble(3, show.getTicketPrice());
            ps.setInt(4, show.getMovieId());
            ps.setInt(5, show.getScreenId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Show added."); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addShow: " + e.getMessage());
        }
        return false;
    }

    public boolean updateShowPrice(int showId, double newPrice) throws DatabaseConnectionException {
        String sql = "UPDATE Shows SET TicketPrice=? WHERE ShowID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, showId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Show price updated."); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateShowPrice: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteShow(int showId) throws DatabaseConnectionException {
        String sql = "DELETE FROM Shows WHERE ShowID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Show deleted: ID " + showId); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] deleteShow: " + e.getMessage());
        }
        return false;
    }

    public List<Show> getShowsByMovie(int movieId) throws DatabaseConnectionException {
        List<Show> list = new ArrayList<>();
        String sql = "SELECT * FROM Shows WHERE MovieID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapShow(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getShowsByMovie: " + e.getMessage());
        }
        return list;
    }

    public List<Show> getAllShows() throws DatabaseConnectionException {
        List<Show> list = new ArrayList<>();
        String sql = "SELECT * FROM Shows";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapShow(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllShows: " + e.getMessage());
        }
        return list;
    }

    private Show mapShow(ResultSet rs) throws SQLException {
        return new Show(rs.getInt("ShowID"), rs.getDate("ShowDate"),
                rs.getTime("ShowTime"), rs.getDouble("TicketPrice"),
                rs.getInt("MovieID"), rs.getInt("ScreenID"));
    }
}