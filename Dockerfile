# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Cache Gradle wrapper + dependencies
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null

COPY src src
RUN ./gradlew bootJar --no-daemon -x test && cp build/libs/*.jar app.jar

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd --system --uid 1001 spring
USER spring

COPY --from=build /app/app.jar app.jar

# Business timezone: "now" checks (e.g. @Future on startTime) and today/month stats use it.
# Container default is UTC, which rejects same-evening bookings. Override with -e TZ=... if needed.
ENV PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    TZ=America/Toronto
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
