package com.traceme.servlet;

import com.google.gson.Gson;
import com.traceme.model.User;
import com.traceme.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/users")
public class UserServlet extends HttpServlet {
    
    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        gson = new Gson();
        System.out.println("[SERVLET INIT] UserServlet initialized - Ready for tracing");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("\n[HTTP REQUEST] UserServlet.doGet() - Entry point");
        System.out.println("[REQUEST URI] " + request.getRequestURI());
        System.out.println("[QUERY STRING] " + request.getQueryString());
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String userId = request.getParameter("id");
        System.out.println("[UNTRUSTED INPUT] User ID parameter: " + userId);
        System.out.println("[DATA FLOW] HTTP Request -> Servlet -> Service Layer");
        Thread.dumpStack();
        PrintWriter out = response.getWriter();
        
        if (userId != null) {
            // VULNERABILITY PATH: User input flows to SQL query
            System.out.println("[VULNERABILITY PATH] Untrusted input -> getUserById()");
            System.out.println("[CALL CHAIN] HTTP -> Servlet -> UserService.getUserById() -> SQL");
            
            // Get specific user - SQL injection vulnerability
            User user = userService.getUserById(userId);
            
            System.out.println("[RESPONSE] Returning user data");
            out.print(gson.toJson(user));
        } else {
            // Get all users
            System.out.println("[SAFE PATH] Getting all users - no untrusted input");
            List<User> users = userService.getAllUsers();
            out.print(gson.toJson(users));
        }
        
        out.flush();
        System.out.println("[HTTP RESPONSE] Request completed\n");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        
        User newUser = userService.createUser(username, email);
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(newUser));
        out.flush();
    }
}
