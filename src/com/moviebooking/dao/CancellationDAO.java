package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.model.Cancellation;
import com.moviebooking.model.Refund;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CancellationDAO {

    public int addCancellation(Cancellation c) throws DatabaseConnectionException {
        String sql = "INSERT INTO Cancellation (Reason, CancellationDate, BookingID) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getReason());
            ps.setDate(2, c.getCancellationDate());
            ps.setInt(3, c.getBookingId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("[OK] Cancellation recorded. CancelID: " + newId);
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addCancellation: " + e.getMessage());
        }
        return -1;
    }

    public Cancellation getCancellationByBooking(int bookingId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM Cancellation WHERE BookingID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Cancellation(rs.getInt("CancelID"),
                    rs.getString("Reason"), rs.getDate("CancellationDate"), rs.getInt("BookingID"));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getCancellationByBooking: " + e.getMessage());
        }
        return null;
    }

    public boolean addRefund(Refund refund) throws DatabaseConnectionException {
        String sql = "INSERT INTO Refund (RefundStatus, RefundAmount, PaymentID, CancelID) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, refund.getRefundStatus());
            ps.setDouble(2, refund.getRefundAmount());
            ps.setInt(3, refund.getPaymentId());
            ps.setInt(4, refund.getCancelId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Refund initiated."); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addRefund: " + e.getMessage());
        }
        return false;
    }

    public boolean updateRefundStatus(int refundId, String status) throws DatabaseConnectionException {
        String sql = "UPDATE Refund SET RefundStatus=? WHERE RefundID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, refundId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Refund status updated: " + status); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateRefundStatus: " + e.getMessage());
        }
        return false;
    }

    public List<Refund> getAllRefunds() throws DatabaseConnectionException {
        List<Refund> list = new ArrayList<>();
        String sql = "SELECT * FROM Refund";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Refund(rs.getInt("RefundID"),
                    rs.getString("RefundStatus"), rs.getDouble("RefundAmount"),
                    rs.getInt("PaymentID"), rs.getInt("CancelID")));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllRefunds: " + e.getMessage());
        }
        return list;
    }
}