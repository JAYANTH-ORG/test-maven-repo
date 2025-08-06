package com.secureflow.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Vulnerable Spring Boot application for security testing
 * Contains intentional security vulnerabilities for SecureFlow to detect
 */
@SpringBootApplication
@RestController
public class VulnerableApplication {

    private static final Logger logger = LogManager.getLogger(VulnerableApplication.class);
    
    // Hardcoded credentials - should be detected by secret scanning
    private static final String DB_PASSWORD = "admin123!@#";
    private static final String API_KEY = "sk-1234567890abcdef1234567890abcdef";
    private static final String JWT_SECRET = "mySecretKey123456789";

    public static void main(String[] args) {
        SpringApplication.run(VulnerableApplication.class, args);
    }

    /**
     * SQL Injection vulnerability - should be detected by SAST
     */
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:h2:mem:testdb", "sa", DB_PASSWORD);
            
            // Vulnerable SQL query - direct string concatenation
            String sql = "SELECT * FROM users WHERE id = '" + id + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return "User: " + rs.getString("name");
            }
            
            conn.close();
        } catch (SQLException e) {
            logger.error("Database error: " + e.getMessage());
        }
        return "User not found";
    }

    /**
     * Path Traversal vulnerability - should be detected by SAST
     */
    @GetMapping("/file/{filename}")
    public String readFile(@PathVariable String filename) {
        try {
            // Vulnerable file access - no path validation
            File file = new File("/app/data/" + filename);
            FileInputStream fis = new FileInputStream(file);
            
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            
            return new String(data);
        } catch (IOException e) {
            logger.error("File read error: " + e.getMessage());
            return "Error reading file";
        }
    }

    /**
     * Command Injection vulnerability - should be detected by SAST
     */
    @PostMapping("/ping")
    public String pingHost(@RequestParam String host) {
        try {
            // Vulnerable command execution
            String command = "ping -c 1 " + host;
            Process process = Runtime.getRuntime().exec(command);
            
            return "Ping executed for: " + host;
        } catch (IOException e) {
            logger.error("Command execution error: " + e.getMessage());
            return "Ping failed";
        }
    }

    /**
     * XXE vulnerability - should be detected by SAST
     */
    @PostMapping("/xml")
    public String processXml(@RequestBody String xmlData) {
        try {
            // Vulnerable XML parsing - XXE enabled
            javax.xml.parsers.DocumentBuilderFactory factory = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            // XXE vulnerability - external entities not disabled
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            
            java.io.StringReader reader = new java.io.StringReader(xmlData);
            org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
            
            builder.parse(source);
            return "XML processed successfully";
        } catch (Exception e) {
            logger.error("XML processing error: " + e.getMessage());
            return "XML processing failed";
        }
    }

    /**
     * Insecure random number generation - should be detected by SAST
     */
    @GetMapping("/token")
    public String generateToken() {
        // Vulnerable random number generation
        java.util.Random random = new java.util.Random();
        long token = random.nextLong();
        
        return "Token: " + token;
    }

    /**
     * Weak cryptography - should be detected by SAST
     */
    @PostMapping("/encrypt")
    public String encryptData(@RequestParam String data) {
        try {
            // Vulnerable encryption - MD5 is cryptographically broken
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(data.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            
            return "Encrypted: " + sb.toString();
        } catch (Exception e) {
            logger.error("Encryption error: " + e.getMessage());
            return "Encryption failed";
        }
    }

    /**
     * Information disclosure - should be detected by SAST
     */
    @GetMapping("/config")
    public String getConfig() {
        Properties props = new Properties();
        
        // Exposing sensitive configuration
        props.put("database.password", DB_PASSWORD);
        props.put("api.key", API_KEY);
        props.put("jwt.secret", JWT_SECRET);
        
        return props.toString();
    }
}
