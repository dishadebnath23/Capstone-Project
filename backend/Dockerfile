# Use lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot fat jar
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose backend port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
