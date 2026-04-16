package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.model.Theatre;
import com.moviebooking.model.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheatreDAO {

    public boolean addTheatre(Theatre t) throws DatabaseConnectionException {
        String sql = "INSERT INTO Theatre (Name, Location) VALUES (?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getLocation());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Theatre added: " + t.getName()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addTheatre: " + e.getMessage());
        }
        return false;
    }

    public boolean updateTheatre(Theatre t) throws DatabaseConnectionException {
        String sql = "UPDATE Theatre SET Name=?, Location=? WHERE TheatreID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getLocation());
            ps.setInt(3, t.getTheatreId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Theatre updated."); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateTheatre: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteTheatre(int id) throws DatabaseConnectionException {
        String sql = "DELETE FROM Theatre WHERE TheatreID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Theatre deleted: ID " + id); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] deleteTheatre: " + e.getMessage());
        }
        return false;
    }

    public List<Theatre> getAllTheatres() throws DatabaseConnectionException {
        List<Theatre> list = new ArrayList<>();
        String sql = "SELECT * FROM Theatre";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Theatre(rs.getInt("TheatreID"),
                    rs.getString("Name"), rs.getString("Location")));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllTheatres: " + e.getMessage());
        }
        return list;
    }

    public List<Seat> getAvailableSeats(int showId, int screenId) throws DatabaseConnectionException {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT s.* FROM Seat s WHERE s.ScreenID = ? " +
                     "AND s.SeatID NOT IN (SELECT SeatID FROM Booking WHERE ShowID=? AND Status='CONFIRMED')";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, screenId);
            ps.setInt(2, showId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Seat(rs.getInt("SeatID"),
                    rs.getString("SeatNumber"), rs.getString("SeatType"), rs.getInt("ScreenID")));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAvailableSeats: " + e.getMessage());
        }
        return list;
    }
}