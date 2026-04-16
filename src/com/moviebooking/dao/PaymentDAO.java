package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.exception.InvalidPaymentException;
import com.moviebooking.model.Payment;
import com.moviebooking.model.DigitalInvoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public int addPayment(Payment payment) throws DatabaseConnectionException, InvalidPaymentException {
        if (payment.getMode() == null || payment.getMode().isEmpty()) {
            throw new InvalidPaymentException("Payment mode cannot be empty.");
        }
        String sql = "INSERT INTO Payment (Mode, PaymentStatus, TransactionDate, BookingID) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, payment.getMode());
            ps.setString(2, payment.getPaymentStatus());
            ps.setDate(3, payment.getTransactionDate());
            ps.setInt(4, payment.getBookingId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("[OK] Payment recorded. PaymentID: " + newId);
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SQL Error] addPayment: " + e.getMessage());
        }
        return -1;
    }

    public boolean updatePaymentStatus(int paymentId, String status) throws DatabaseConnectionException {
        String sql = "UPDATE Payment SET PaymentStatus=? WHERE PaymentID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Payment status updated: " + status); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updatePaymentStatus: " + e.getMessage());
        }
        return false;
    }

    public Payment getPaymentByBooking(int bookingId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM Payment WHERE BookingID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapPayment(rs);
        } catch (SQLException e) {
            System.err.println("[SQL Error] getPaymentByBooking: " + e.getMessage());
        }
        return null;
    }

    public boolean generateInvoice(DigitalInvoice invoice) throws DatabaseConnectionException {
        String sql = "INSERT INTO DigitalInvoice (Date, Tax, PaymentID) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, invoice.getDate());
            ps.setDouble(2, invoice.getTax());
            ps.setInt(3, invoice.getPaymentId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] Invoice generated for PaymentID: " + invoice.getPaymentId()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] generateInvoice: " + e.getMessage());
        }
        return false;
    }

    public DigitalInvoice getInvoiceByPayment(int paymentId) throws DatabaseConnectionException {
        String sql = "SELECT * FROM DigitalInvoice WHERE PaymentID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new DigitalInvoice(rs.getInt("InvoiceID"),
                    rs.getDate("Date"), rs.getDouble("Tax"), rs.getInt("PaymentID"));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getInvoiceByPayment: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> getAllPayments() throws DatabaseConnectionException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapPayment(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllPayments: " + e.getMessage());
        }
        return list;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        return new Payment(rs.getInt("PaymentID"), rs.getString("Mode"),
                rs.getString("PaymentStatus"), rs.getDate("TransactionDate"), rs.getInt("BookingID"));
    }
}