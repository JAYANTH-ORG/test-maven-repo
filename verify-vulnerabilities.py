#!/usr/bin/env python3
"""
Quick test script to verify our vulnerabilities are detectable
"""

import re
import os
import sys

def check_sql_injection():
    """Check for SQL injection patterns"""
    java_files = []
    for root, dirs, files in os.walk('src'):
        for file in files:
            if file.endswith('.java'):
                java_files.append(os.path.join(root, file))
    
    sql_patterns = [
        r'Statement.*executeQuery.*\+',  # SQL injection via string concatenation
        r'SELECT.*FROM.*\+',             # Direct SQL concatenation
        r'sql.*=.*\+.*query',            # SQL string building
    ]
    
    findings = []
    for java_file in java_files:
        with open(java_file, 'r') as f:
            content = f.read()
            for i, line in enumerate(content.split('\n'), 1):
                for pattern in sql_patterns:
                    if re.search(pattern, line, re.IGNORECASE):
                        findings.append(f"SQL Injection in {java_file}:{i} - {line.strip()}")
    
    return findings

def check_dependency_vulnerabilities():
    """Check for vulnerable dependencies in pom.xml"""
    vulnerable_deps = [
        ('jackson-databind', '2.9.8'),
        ('log4j-core', '2.14.1'),
        ('spring-boot', '2.4.0'),
        ('h2', '1.4.199'),
    ]
    
    findings = []
    if os.path.exists('pom.xml'):
        with open('pom.xml', 'r') as f:
            content = f.read()
            for dep_name, version in vulnerable_deps:
                if dep_name in content and version in content:
                    findings.append(f"Vulnerable dependency: {dep_name} version {version}")
    
    return findings

def check_hardcoded_secrets():
    """Check for hardcoded secrets"""
    secret_patterns = [
        r'password.*=.*["\'][^"\']{8,}["\']',
        r'secret.*=.*["\'][^"\']{8,}["\']',
        r'api.*key.*=.*["\'][^"\']{8,}["\']',
        r'token.*=.*["\'][^"\']{8,}["\']',
    ]
    
    files_to_check = []
    for root, dirs, files in os.walk('.'):
        for file in files:
            if file.endswith(('.java', '.properties', '.env', '.txt')):
                files_to_check.append(os.path.join(root, file))
    
    findings = []
    for file_path in files_to_check:
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                for i, line in enumerate(content.split('\n'), 1):
                    for pattern in secret_patterns:
                        if re.search(pattern, line, re.IGNORECASE):
                            findings.append(f"Hardcoded secret in {file_path}:{i} - {line.strip()}")
        except:
            pass  # Skip binary files
    
    return findings

def main():
    print("🔍 Verifying test vulnerabilities are detectable...")
    print("=" * 60)
    
    # Check SQL injection
    sql_findings = check_sql_injection()
    print(f"\n📊 SQL Injection Vulnerabilities: {len(sql_findings)}")
    for finding in sql_findings[:3]:  # Show first 3
        print(f"  ✓ {finding}")
    
    # Check dependencies
    dep_findings = check_dependency_vulnerabilities()
    print(f"\n📦 Vulnerable Dependencies: {len(dep_findings)}")
    for finding in dep_findings:
        print(f"  ✓ {finding}")
    
    # Check secrets
    secret_findings = check_hardcoded_secrets()
    print(f"\n🔐 Hardcoded Secrets: {len(secret_findings)}")
    for finding in secret_findings[:3]:  # Show first 3
        print(f"  ✓ {finding}")
    
    total_issues = len(sql_findings) + len(dep_findings) + len(secret_findings)
    print(f"\n🎯 Total Issues Found: {total_issues}")
    
    if total_issues > 0:
        print("✅ Repository contains detectable vulnerabilities!")
        print("If SecureFlow shows 0 findings, there may be a tool configuration issue.")
    else:
        print("❌ No vulnerabilities detected - check test patterns!")
    
    return 0 if total_issues > 0 else 1

if __name__ == "__main__":
    sys.exit(main())
