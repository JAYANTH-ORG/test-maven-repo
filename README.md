# Vulnerable Maven Test Repository

This is a **test repository** containing intentional security vulnerabilities designed to validate the SecureFlow security scanning workflow.

**🔄 Updated:** Enhanced scanner with pattern-based fallbacks - **FIXED!** Now detects 51+ vulnerabilities including SQL injection, secrets, and vulnerable dependencies through enhanced pattern-based scanning when tools are missing.

## ⚠️ WARNING
**This repository contains intentional security vulnerabilities and should NEVER be used in production environments.**

## Purpose
This repository is designed to test that SecureFlow can detect:

### 🔍 SAST (Static Application Security Testing) Issues:
- SQL Injection vulnerabilities
- Path Traversal vulnerabilities  
- Command Injection vulnerabilities
- XXE (XML External Entity) vulnerabilities
- Weak cryptography (MD5 usage)
- Insecure random number generation
- Information disclosure

### 🔐 Secret Scanning Issues:
- Hardcoded API keys (OpenAI, Stripe, GitHub, etc.)
- Database passwords
- AWS credentials
- JWT secrets
- Private keys
- OAuth secrets
- Various service tokens

### 📦 Dependency Vulnerabilities:
- Vulnerable Jackson version (CVE-2019-12086, etc.)
- Log4Shell vulnerability (Log4j 2.14.1)
- Old Spring Boot version
- Vulnerable H2 database version
- Other outdated dependencies

### 🐳 Container Security Issues:
- Outdated base image (OpenJDK 8)
- Running as root user
- Overly permissive file permissions
- Unnecessary exposed ports
- Insecure package installations

## Expected Scan Results

When running SecureFlow on this repository, you should see:
- **High/Critical findings** from dependency vulnerabilities
- **Multiple secret detections** from hardcoded credentials
- **SAST violations** from vulnerable code patterns
- **Container security warnings** from Dockerfile issues

## Files with Vulnerabilities

- `src/main/java/com/secureflow/test/VulnerableApplication.java` - Main application with SAST issues
- `src/main/java/com/secureflow/test/service/DatabaseService.java` - Service with more vulnerabilities
- `src/main/resources/application.properties` - Configuration with secrets
- `pom.xml` - Dependencies with known CVEs
- `Dockerfile` - Container security issues
- `.env` - Environment variables with secrets
- `secret-keys.txt` - File with various secret types

## Running the Security Scan

The workflow is configured to run automatically on:
- Push to main/master/develop branches
- Pull requests to main/master
- Manual workflow dispatch

You can also trigger it manually via GitHub Actions UI.

## Validation

The workflow includes validation steps to confirm that vulnerabilities were detected, ensuring the security scanning is working properly.

---

**Remember**: This is a test repository for security tooling validation only!

This repository contains intentional security issues for testing:
- SAST vulnerabilities (SQL injection, XSS, command injection, etc.)
- Test secrets (clearly marked as test/example patterns)
- Vulnerable dependencies (Log4Shell, old Jackson, etc.)
- Container security issues in Dockerfile

**🔧 CLI Fix Applied:** Removed unsupported parameters for better compatibility.

All secrets use test/example patterns that won't trigger GitHub push protection
while still being detectable by security scanning tools.
