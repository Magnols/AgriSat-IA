FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src src
RUN chmod +x mvnw && ./mvnw -B -ntp clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S agrisat && adduser -S agrisat -G agrisat
COPY --from=build --chown=agrisat:agrisat /workspace/target/energia-esg-api-0.0.1-SNAPSHOT.jar app.jar
USER agrisat
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=5 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
