package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    // ABSOLUTE path — guaranteed to show up on your Desktop
    private static final String DB_PATH = "C:/Users/14374/Desktop/bank.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connected to SQLite: " + DB_PATH);
                Statement stmt = conn.createStatement();

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        email TEXT PRIMARY KEY,
                        password TEXT NOT NULL,
                        role TEXT NOT NULL,
                        balance REAL DEFAULT 0
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_email TEXT,
                        amount REAL,
                        type TEXT,
                        timestamp TEXT,
                        FOREIGN KEY(user_email) REFERENCES users(email)
                    );
                """);

                System.out.println("✅ Tables created (or already exist).");
            } else {
                System.out.println("Connection was null.");
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
