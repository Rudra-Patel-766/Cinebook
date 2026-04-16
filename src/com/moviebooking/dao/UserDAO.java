package com.moviebooking.dao;

import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.DatabaseConnectionException;
import com.moviebooking.exception.UserNotFoundException;
import com.moviebooking.model.User;
import com.moviebooking.model.Customer;
import com.moviebooking.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(User user) throws DatabaseConnectionException {
        String sql = "INSERT INTO User (Name, Email, Phone, Password, Role) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] User registered: " + user.getName()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] registerUser: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUser(User user) throws DatabaseConnectionException {
        String sql = "UPDATE User SET Name=?, Email=?, Phone=? WHERE UserID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getUserId());
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] User updated: ID " + user.getUserId()); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] updateUser: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(int userId) throws DatabaseConnectionException {
        String sql = "DELETE FROM User WHERE UserID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            if (rows > 0) { System.out.println("[OK] User deleted: ID " + userId); return true; }
        } catch (SQLException e) {
            System.err.println("[SQL Error] deleteUser: " + e.getMessage());
        }
        return false;
    }

    public User getUserById(int userId) throws DatabaseConnectionException, UserNotFoundException {
        String sql = "SELECT * FROM User WHERE UserID=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.err.println("[SQL Error] getUserById: " + e.getMessage());
        }
        throw new UserNotFoundException("ID: " + userId);
    }

    public User login(String email, String password) throws DatabaseConnectionException {
        String sql = "SELECT * FROM User WHERE Email=? AND Password=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.err.println("[SQL Error] login: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() throws DatabaseConnectionException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) users.add(mapUser(rs));
        } catch (SQLException e) {
            System.err.println("[SQL Error] getAllUsers: " + e.getMessage());
        }
        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("UserID"));
        u.setName(rs.getString("Name"));
        u.setEmail(rs.getString("Email"));
        u.setPhone(rs.getString("Phone"));
        u.setPassword(rs.getString("Password"));
        u.setRole(rs.getString("Role"));
        return u;
    }
}