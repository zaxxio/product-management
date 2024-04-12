# Use AdoptOpenJDK's OpenJDK 17 image as base
FROM openjdk:23-ea-17-jdk-bullseye

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY /product-service/target/application.jar /app/application.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "application.jar"]
