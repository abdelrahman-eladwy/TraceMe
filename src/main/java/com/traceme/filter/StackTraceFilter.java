package com.traceme.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class StackTraceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Enable detailed stack traces for IAST
        System.setProperty("java.security.debug", "all");
        System.setProperty("javax.net.debug", "all");
        
        // Enable verbose exception handling
        System.setProperty("sun.misc.URLClassPath.debug", "true");
        System.setProperty("sun.misc.URLClassPath.debugLookups", "true");
        
        System.out.println("=== Stack Trace Filter Initialized ===");
        System.out.println("Application is now fully traceable for IAST");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Log request for tracing
        logRequest(httpRequest);
        
        try {
            // Execute the request
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Print full stack trace for IAST to capture
            System.err.println("=== Exception in request: " + httpRequest.getRequestURI() + " ===");
            e.printStackTrace(System.err);
            
            // Log the complete stack trace
            logStackTrace(e);
            
            // Re-throw to allow normal error handling
            throw e;
        }
    }

    @Override
    public void destroy() {
        System.out.println("=== Stack Trace Filter Destroyed ===");
    }
    
    private void logRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Request Trace ===\n");
        sb.append("URI: ").append(request.getRequestURI()).append("\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("Query: ").append(request.getQueryString()).append("\n");
        sb.append("Remote Addr: ").append(request.getRemoteAddr()).append("\n");
        sb.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
        sb.append("Stack Trace:\n");
        
        // Print current stack trace for IAST to track execution path
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            sb.append("  at ").append(element.toString()).append("\n");
        }
        
        System.out.println(sb.toString());
    }
    
    private void logStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Full Stack Trace for IAST ===\n");
        sb.append("Exception: ").append(t.getClass().getName()).append("\n");
        sb.append("Message: ").append(t.getMessage()).append("\n");
        sb.append("Cause: ").append(t.getCause()).append("\n");
        
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
        }
        
        if (t.getCause() != null && t.getCause() != t) {
            sb.append("\nCaused by:\n");
            logCause(t.getCause(), sb);
        }
        
        System.err.println(sb.toString());
    }
    
    private void logCause(Throwable t, StringBuilder sb) {
        sb.append(t.getClass().getName()).append(": ").append(t.getMessage()).append("\n");
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
        }
    }
}
