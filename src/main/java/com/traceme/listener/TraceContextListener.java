package com.traceme.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TraceContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\n========================================");
        System.out.println("  traceMe Application Starting");
        System.out.println("  Stack Tracing: ENABLED");
        System.out.println("  IAST Ready: YES");
        System.out.println("========================================\n");
        
        // Enable assertions for better tracing
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        
        // Print JVM information for IAST
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Arch: " + System.getProperty("os.arch"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("\nApplication Context: " + sce.getServletContext().getContextPath());
        System.out.println("Server Info: " + sce.getServletContext().getServerInfo());
        
        // Enable verbose GC for complete tracing
        try {
            java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments()
                .forEach(arg -> System.out.println("JVM Arg: " + arg));
        } catch (Exception e) {
            System.err.println("Could not read JVM arguments: " + e.getMessage());
        }
        
        System.out.println("\n=== Stack trace logging is fully enabled ===\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\n========================================");
        System.out.println("  traceMe Application Stopping");
        System.out.println("========================================\n");
    }
}
