package com.traceme.service;

import com.traceme.model.User;
import com.traceme.aspect.MethodTracer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public UserService() {
        initDatabase();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Insert sample data
            stmt.execute("INSERT INTO users (username, email) VALUES " +
                    "('admin', 'admin@taceme.com'), " +
                    "('user1', 'user1@taceme.com'), " +
                    "('demo', 'demo@taceme.com')");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(String id) {
        MethodTracer.traceMethodEntry(this, "getUserById", id);
        
        // Intentionally vulnerable to SQL injection for IAST testing
        String query = "SELECT * FROM users WHERE id = " + id;
        System.out.println("[TRACE] Executing SQL: " + query);
        System.out.println("[TRACE] Stack at query execution:");
        Thread.dumpStack();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getString("created_at"));
                MethodTracer.traceMethodExit(this, "getUserById", user);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getString("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User createUser(String username, String email) {
        String query = "INSERT INTO users (username, email) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return getUserById(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
