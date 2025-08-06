# Intentionally vulnerable Dockerfile for security testing
FROM openjdk:8-jre-slim  # Outdated and vulnerable base image

# Running as root user (security risk)
USER root

# Installing packages without version pinning (security risk)
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    netcat \
    telnet \
    && rm -rf /var/lib/apt/lists/*

# Copying sensitive files (should trigger warnings)
COPY secret-keys.txt /app/
COPY .env /app/

# Setting environment variables with secrets (bad practice)
ENV DATABASE_PASSWORD=admin123
ENV API_KEY=sk-1234567890abcdef
ENV JWT_SECRET=mySecretJWTKey

# Exposing unnecessary ports
EXPOSE 22 23 80 443 3306 5432 6379 8080

# Creating directories with overly permissive permissions
RUN mkdir -p /app/data && chmod 777 /app/data

# Downloading and running software from internet without verification
RUN curl -sSL https://example.com/install.sh | bash

# Using ADD instead of COPY for local files (security risk)
ADD target/vulnerable-maven-app-1.0-SNAPSHOT.jar /app/app.jar

# Working directory setup
WORKDIR /app

# Running application as root (security risk)
CMD ["java", "-jar", "app.jar"]

# Health check without proper validation
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1
