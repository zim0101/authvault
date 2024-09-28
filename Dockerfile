# Use Maven to build the project with Java 21 (via eclipse-temurin)
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the application source code
COPY src ./src
COPY .env .env
RUN mvn clean package -DskipTests

# Use OpenJDK 21 to run the Spring Boot application
FROM openjdk:21-jdk-slim

# Set a working directory for the application
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/authvault-0.0.1-SNAPSHOT.jar app.jar

# Copy the .env file from the build stage
COPY --from=build /app/.env .env

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
