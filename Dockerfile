# Stage 1: Build the application
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM openjdk:17-oracle
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "app.jar"]
