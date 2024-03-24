# Use AdoptOpenJDK 21 as the base image
FROM adoptopenjdk/openjdk16:alpine-jre AS build

# Install Maven
RUN apk add --no-cache maven

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the rest of the application code
COPY src ./src

# Package the application
RUN mvn clean package

# Use AdoptOpenJDK 21 as the base image
FROM adoptopenjdk/openjdk16:alpine-jre

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file from the build stage to the container
COPY --from=build /app/target/UPBEAT-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
