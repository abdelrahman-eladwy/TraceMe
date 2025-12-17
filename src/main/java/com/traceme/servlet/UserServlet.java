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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String userId = request.getParameter("id");
        PrintWriter out = response.getWriter();
        
        if (userId != null) {
            // Get specific user - potential SQL injection point for testing
            User user = userService.getUserById(userId);
            out.print(gson.toJson(user));
        } else {
            // Get all users
            List<User> users = userService.getAllUsers();
            out.print(gson.toJson(users));
        }
        
        out.flush();
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
