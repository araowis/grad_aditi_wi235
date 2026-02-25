# build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY docker_java_postgres/ .
RUN mvn clean package -DskipTests

# run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/docker_java_postgres-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]