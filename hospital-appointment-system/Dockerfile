# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first so dependency downloads are cached across builds
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/hospital-appointment-system.jar app.jar

# Render sets $PORT at runtime; application.properties already reads it
# via server.port=${PORT:8080}.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
