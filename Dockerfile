FROM maven:3.8.4-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
FROM adoptopenjdk/openjdk16:alpine-jre
WORKDIR /app
COPY --from=build /app/target/UPBEAT-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
