# Use an official Gradle image to build the application with Java 21
FROM gradle:8.10.2-jdk21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle project files to the container
COPY build.gradle settings.gradle /app/
COPY src /app/src
COPY src/main/resources /app/src/main/resources


# Run Gradle to build the application with the shadow plugin
RUN gradle clean shadowJar --no-daemon

# Use a lightweight Java 21 image to run the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built shadow JAR from the build stage
COPY --from=build /app/build/libs/app-1.0.jar /app/app.jar

# Expose the application's port (if applicable)
EXPOSE 8080

# Set environment variables
ENV DOTENV_FILE_PATH=/app/.env

# Copy the .env file to the container (optional, if using dotenv for environment variables)
COPY .env /app/.env

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]