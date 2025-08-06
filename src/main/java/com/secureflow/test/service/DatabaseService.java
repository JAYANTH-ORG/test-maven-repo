package com.secureflow.test.service;

import org.springframework.stereotype.Service;
import java.util.Properties;

/**
 * Service class with hardcoded secrets and vulnerabilities
 */
@Service
public class DatabaseService {

    // Various types of hardcoded secrets that should be detected
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String GITHUB_TOKEN = "ghp_1234567890abcdef1234567890abcdef123456";
    private static final String SLACK_WEBHOOK = "https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String SENDGRID_API_KEY = "SG.1234567890abcdef.1234567890abcdef1234567890abcdef1234567890abcdef";

    // Database credentials
    private final String dbUrl = "jdbc:postgresql://prod-db.company.com:5432/maindb";
    private final String dbUser = "admin";
    private final String dbPass = "P@ssw0rd123!";

    // API credentials
    private final String stripeSecretKey = "sk_live_1234567890abcdef1234567890abcdef";
    private final String twilioAuthToken = "1234567890abcdef1234567890abcdef12";

    /**
     * Vulnerable method with SQL injection
     */
    public String getUserData(String userId) {
        String query = "SELECT * FROM users WHERE id = '" + userId + "'";
        // This would execute the vulnerable query
        return executeQuery(query);
    }

    /**
     * Method with hardcoded encryption key
     */
    public String encryptSensitiveData(String data) {
        String encryptionKey = "MySecretEncryptionKey123456789012"; // 32 chars for AES
        
        try {
            // Vulnerable: hardcoded key
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            javax.crypto.spec.SecretKeySpec keySpec = 
                new javax.crypto.spec.SecretKeySpec(encryptionKey.getBytes(), "AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return java.util.Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return "Encryption failed";
        }
    }

    /**
     * Method that loads properties with secrets
     */
    public Properties loadConfiguration() {
        Properties config = new Properties();
        
        // Loading sensitive configuration
        config.setProperty("aws.access.key", AWS_ACCESS_KEY);
        config.setProperty("aws.secret.key", AWS_SECRET_KEY);
        config.setProperty("github.token", GITHUB_TOKEN);
        config.setProperty("slack.webhook.url", SLACK_WEBHOOK);
        config.setProperty("sendgrid.api.key", SENDGRID_API_KEY);
        config.setProperty("database.password", dbPass);
        config.setProperty("stripe.secret.key", stripeSecretKey);
        config.setProperty("twilio.auth.token", twilioAuthToken);
        
        return config;
    }

    private String executeQuery(String query) {
        // Mock implementation
        return "Query result for: " + query;
    }
}
