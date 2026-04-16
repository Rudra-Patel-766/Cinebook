package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.exception.SeatNotAvailableException;
import com.moviebooking.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean isSeatAvailable(int showId, int seatId) throws DatabaseConnectionException {
        String sql = "SELECT COUNT(*) FROM Booking WHERE ShowID=? AND SeatID=? AND Status='CONFIRMED'";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showId);
            ps.setInt(2, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) {
            System.err.println("[SQL Error] isSeatAvailable: " + e.getMessage());
        }
        return false;
    }

    public int addBooking(Booking booking) throws DatabaseConnectionException, SeatNotAvailableException {
        if (!isSeatAvailable(booking.getShowId(), booking.getSeatId())) {
            throw new SeatNotAvailableException(String.valueOf(booking.getSeatId()));
        }
        String sql = "INSERT INTO Booking (BookingDate, Status, TotalAmount, UserID, ShowID, SeatID) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, booking.getBookingDate());
            ps.setString(2, booking.getStatus());
            ps.setDouble(3, booking.getTotalAmount());
            ps.setInt(4, booking.getUserId());
            ps.setInt(5, booking.getShowId());
            ps.setInt(6, booking.getSeatId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("[OK] Booking confirmed. BookingID: " + newId);
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addBooking: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateBookingStatus(int bookingId, String status) throws DatabaseConnectionException {
        String sql = "UPDATE Booking SET Status=? WHERE BookingID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Booking status updated to: " + status); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateBookingStatus: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteBooking(int bookingId) throws DatabaseConnectionException {
        String sql = "DELETE FROM Booking WHERE BookingID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Booking deleted: ID " + bookingId); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] deleteBooking: " + e.getMessage());
        }
        return false;
    }

    public List<Booking> getBookingsByUser(int userId) throws DatabaseConnectionException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE UserID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBooking(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getBookingsByUser: " + e.getMessage());
        }
        return list;
    }

    public Booking getBookingById(int bookingId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM Booking WHERE BookingID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBooking(rs);
        } catch (SQLException e) {
            System.err.println("[SQL Error] getBookingById: " + e.getMessage());
        }
        return null;
    }

    public List<Booking> getAllBookings() throws DatabaseConnectionException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM Booking ORDER BY BookingDate DESC";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapBooking(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllBookings: " + e.getMessage());
        }
        return list;
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {
        return new Booking(rs.getInt("BookingID"), rs.getDate("BookingDate"),
                rs.getString("Status"), rs.getDouble("TotalAmount"),
                rs.getInt("UserID"), rs.getInt("ShowID"), rs.getInt("SeatID"));
    }
}