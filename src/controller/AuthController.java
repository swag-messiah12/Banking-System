package controller;

import model.DBUtil;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthController {
    // Login method: searches for a user by email and password.

    public static User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                double balance = rs.getDouble("balance");
                return new User(email, password, role, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Creates a default admin
    public static void createDefaultAdmin() {
        String checkSql = "SELECT * FROM users WHERE email = 'admin@bank.com'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             ResultSet rs = checkStmt.executeQuery()) {
            if (!rs.next()) {
                String insertSql = "INSERT INTO users (email, password, role, balance) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "admin@bank.com");
                    insertStmt.setString(2, "admin123");  // Use proper hashing in production!
                    insertStmt.setString(3, "admin");
                    insertStmt.setDouble(4, 0.0);
                    insertStmt.executeUpdate();
                    System.out.println("Default admin created.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBalance(String email, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String email, String password, String role, double balance) {
        String sql = "INSERT OR IGNORE INTO users (email, password, role, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password); // Add hashing later
            stmt.setString(3, role);
            stmt.setDouble(4, balance);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("email"),
                        rs.getString("password"),  // You can exclude it if needed
                        rs.getString("role"),
                        rs.getDouble("balance")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void createSampleUsers() {
        try (Connection conn = DBUtil.getConnection()) {
            String insert = "INSERT OR IGNORE INTO users (email, password, role, balance) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(insert);

            // Add a sample customer
            stmt.setString(1, "customer@bank.com");
            stmt.setString(2, "customer123");
            stmt.setString(3, "customer");
            stmt.setDouble(4, 500.0);
            stmt.executeUpdate();

            // Add a sample employee
            stmt.setString(1, "employee@bank.com");
            stmt.setString(2, "employee123");
            stmt.setString(3, "employee");
            stmt.setDouble(4, 0.0);
            stmt.executeUpdate();

            System.out.println(" Sample customer and employee created.");
        } catch (SQLException e) {
            System.out.println(" Failed to create sample users: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






}
