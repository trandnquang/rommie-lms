# ==========================================================
# STAGE 1: Build the Application
# ==========================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache dependencies first to speed up future builds
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the JAR file
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================================
# STAGE 2: Run the Application
# ==========================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the generated JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]