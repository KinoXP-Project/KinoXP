# Build stage

# Maven base image with version and JDK version
FROM maven:3.9.11-eclipse-temurin-21 AS build

# Copies the application files into the Docker image
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Package the app without running tests
RUN mvn clean package -DskipTests

# Runtime stage

# Uses a smaller base image
FROM eclipse-temurin:21-jre-alpine

# Copies JAR file from build stage and renames it to app.jar
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Sets the entrypoint for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
