
# STAGE 1: BUILD THE SPRING BOOT APPLICATION
FROM maven:3.10-eclipse-temurin-25-noble AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package


# STAGE 2: RUN THE APPLICATION
FROM eclipse-temurin:25-jre-noble

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
