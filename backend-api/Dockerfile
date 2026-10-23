FROM eclipse-temurin:25-jdk-noble AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN tr -d '\r' < mvnw > mvnw.lf \
    && mv mvnw.lf mvnw \
    && chmod +x mvnw

COPY src ./src

RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre-noble

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
