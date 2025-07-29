# Use minimal JDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Accept a JAR file as build argument
ARG JAR_FILE=target/*.jar

# Copy the built jar into the image
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
