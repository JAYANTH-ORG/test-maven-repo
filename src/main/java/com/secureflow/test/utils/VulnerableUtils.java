package com.secureflow.test.utils;

import java.sql.*;
import java.io.*;
import java.util.Scanner;

/**
 * Additional vulnerable utility class with obvious security issues
 */
public class VulnerableUtils {
    
    // Hardcoded password - obvious secret
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/test";
    
    /**
     * Classic SQL injection vulnerability
     */
    public static String getUserByName(String username) throws SQLException {
        Connection conn = DriverManager.getConnection(DATABASE_URL, "root", ADMIN_PASSWORD);
        
        // VULNERABLE: Direct string concatenation in SQL query
        String query = "SELECT * FROM users WHERE username = '" + username + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            return rs.getString("email");
        }
        return null;
    }
    
    /**
     * Command injection vulnerability
     */
    public static String executeCommand(String userInput) {
        try {
            // VULNERABLE: Direct execution of user input
            Process proc = Runtime.getRuntime().exec("ping " + userInput);
            Scanner scanner = new Scanner(proc.getInputStream());
            StringBuilder output = new StringBuilder();
            while (scanner.hasNextLine()) {
                output.append(scanner.nextLine()).append("\n");
            }
            return output.toString();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Path traversal vulnerability
     */
    public static String readFile(String fileName) {
        try {
            // VULNERABLE: No path validation
            File file = new File("/app/data/" + fileName);
            Scanner scanner = new Scanner(file);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            return content.toString();
        } catch (FileNotFoundException e) {
            return "File not found: " + fileName;
        }
    }
    
    /**
     * Weak cryptography
     */
    public static String hashPassword(String password) {
        try {
            // VULNERABLE: MD5 is cryptographically broken
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Insecure random number generation
     */
    public static String generateSessionToken() {
        // VULNERABLE: Using java.util.Random for security tokens
        java.util.Random random = new java.util.Random();
        return String.valueOf(random.nextLong());
    }
}
