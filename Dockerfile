# Maven base image with version and JDK version
FROM maven:3.9.11-eclipse-temurin-21

# Copies the application files into the Docker image
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Builds the application using Maven
RUN mvn clean package -DskipTests

# Extracts the JAR file from target directory
RUN cp target/*.jar app.jar

# Sets the entrypoint for the container
ENTRYPOINT ["java", "-jar", "app.jar"]




