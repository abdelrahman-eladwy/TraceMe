package com.traceme.service;

import java.sql.*;

public class AuthService {
    
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public AuthService() {
        initDatabase();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS credentials (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(100) NOT NULL)");
            
            // Insert sample credentials (weak passwords for demo)
            stmt.execute("INSERT INTO credentials (username, password) VALUES " +
                    "('admin', 'admin123'), " +
                    "('user1', 'password'), " +
                    "('demo', 'demo')");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password) {
        // Intentionally vulnerable SQL query for IAST testing
        String query = "SELECT * FROM credentials WHERE username = '" + username + 
                       "' AND password = '" + password + "'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
