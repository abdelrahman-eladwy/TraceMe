package com.traceme.service;

import com.traceme.aspect.MethodTracer;

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
        MethodTracer.traceMethodEntry(this, "authenticate", username, "***");
        
        // Intentionally vulnerable SQL query for IAST testing
        String query = "SELECT * FROM credentials WHERE username = '" + username + 
                       "' AND password = '" + password + "'";
        System.out.println("[TRACE] Auth SQL: " + query);
        System.out.println("[TRACE] Stack at auth query:");
        Thread.dumpStack();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            boolean result = rs.next();
            MethodTracer.traceMethodExit(this, "authenticate", result);
            return result;
        } catch (SQLException e) {
            MethodTracer.traceException(this, "authenticate", e);
            e.printStackTrace();
            MethodTracer.traceMethodExit(this, "authenticate", false);
            return false;
        }
    }
}
