package com.traceme.servlet;

import com.traceme.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("\n[LOGIN ATTEMPT] Authentication request received");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("[UNTRUSTED INPUT] Username: " + username);
        System.out.println("[UNTRUSTED INPUT] Password: *** (hidden)");
        System.out.println("[VULNERABILITY PATH] Login credentials -> AuthService.authenticate()");
        System.out.println("[CALL STACK] HTTP POST -> LoginServlet -> AuthService -> SQL");
        Thread.dumpStack();
        
        // VULNERABILITY: Authentication SQL Injection
        if (authService.authenticate(username, password)) {
            System.out.println("[AUTH SUCCESS] User authenticated: " + username);
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            response.sendRedirect("dashboard");
        } else {
            System.out.println("[AUTH FAILED] Invalid credentials for: " + username);
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
        System.out.println("[LOGIN COMPLETE] Authentication flow finished\\n");
    }
}
