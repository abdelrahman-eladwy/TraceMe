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
        
        // VULNERABILITY: SQL Injection in Authentication
        System.out.println("\n[VULNERABILITY] Authentication SQL Injection!");
        System.out.println("[TAINT SOURCE] Username: " + username);
        System.out.println("[TAINT SOURCE] Password: *** (hidden)");
        System.out.println("[TAINT SINK] Authentication query");
        
        // Build vulnerable query - expose in call stack
        String query = buildAuthQuery(username, password);
        
        System.out.println("[DANGEROUS AUTH SQL] " + query);
        System.out.println("[MEMORY TRACE] Authentication vulnerability stack:");
        Thread.dumpStack();
        
        // Execute vulnerable authentication
        boolean result = executeAuthQuery(query);
        return result;
    }
    
    // Expose vulnerability construction in call stack
    private String buildAuthQuery(String username, String password) {
        System.out.println("[VULNERABLE AUTH] Building query with untrusted credentials");
        Thread.dumpStack();
        String query = "SELECT * FROM credentials WHERE username = '" + username + 
                       "' AND password = '" + password + "'";
        return query;
    }
    
    // Execute authentication query - traceable in stack
    private boolean executeAuthQuery(String query) {
        System.out.println("[EXECUTING AUTH QUERY] " + query);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            boolean result = rs.next();
            System.out.println("[AUTH RESULT] " + (result ? "SUCCESS" : "FAILED"));
            MethodTracer.traceMethodExit(this, "executeAuthQuery", result);
            return result;
        } catch (SQLException e) {
            System.err.println("[AUTH SQL EXCEPTION] " + e.getMessage());
            MethodTracer.traceException(this, "executeAuthQuery", e);
            e.printStackTrace();
            MethodTracer.traceMethodExit(this, "executeAuthQuery", false);
            return false;
        }
    }
}
