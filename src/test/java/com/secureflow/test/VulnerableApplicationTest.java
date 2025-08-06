package com.secureflow.test;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic test class for the vulnerable application
 */
public class VulnerableApplicationTest {

    @Test
    public void testApplicationStarts() {
        // Basic test to ensure the application can be compiled
        VulnerableApplication app = new VulnerableApplication();
        assertNotNull(app);
    }

    @Test
    public void testVulnerableEndpoints() {
        VulnerableApplication app = new VulnerableApplication();
        
        // Test some of the vulnerable endpoints
        String userResult = app.getUser("1");
        assertNotNull(userResult);
        
        String tokenResult = app.generateToken();
        assertNotNull(tokenResult);
        assertTrue(tokenResult.startsWith("Token:"));
        
        String configResult = app.getConfig();
        assertNotNull(configResult);
        assertTrue(configResult.contains("password")); // This proves secrets are exposed
    }
}
