package com.traceme.aspect;

import java.lang.reflect.Method;

public class MethodTracer {
    
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
    
    public static void traceMethodEntry(Object instance, String methodName, Object... args) {
        int currentDepth = depth.get();
        depth.set(currentDepth + 1);
        
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent(currentDepth)).append(">>> ENTER: ");
        sb.append(instance.getClass().getSimpleName()).append(".").append(methodName);
        sb.append("(");
        
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(args[i] == null ? "null" : args[i].toString());
            }
        }
        
        sb.append(")");
        sb.append(" [Thread: ").append(Thread.currentThread().getName()).append("]");
        
        System.out.println(sb.toString());
        printCurrentStackTrace(currentDepth + 1);
    }
    
    public static void traceMethodExit(Object instance, String methodName, Object result) {
        int currentDepth = depth.get() - 1;
        depth.set(currentDepth);
        
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent(currentDepth)).append("<<< EXIT: ");
        sb.append(instance.getClass().getSimpleName()).append(".").append(methodName);
        sb.append(" => ").append(result == null ? "void" : result.toString());
        
        System.out.println(sb.toString());
    }
    
    public static void traceException(Object instance, String methodName, Throwable t) {
        int currentDepth = depth.get() - 1;
        depth.set(currentDepth);
        
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent(currentDepth)).append("!!! EXCEPTION in ");
        sb.append(instance.getClass().getSimpleName()).append(".").append(methodName);
        sb.append(": ").append(t.getClass().getSimpleName());
        sb.append(" - ").append(t.getMessage());
        
        System.err.println(sb.toString());
        t.printStackTrace(System.err);
    }
    
    private static String getIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
    
    private static void printCurrentStackTrace(int indent) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String indentStr = getIndent(indent);
        
        for (int i = 3; i < Math.min(stack.length, 8); i++) {
            System.out.println(indentStr + "  at " + stack[i].toString());
        }
    }
}
