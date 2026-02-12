# STAGE-1: BUILD
FROM gradle:9.3-jdk21 AS builder
WORKDIR /app

# 1. Copy the entire project context
COPY . .

# 2. Build & Install Entity Service to the container's local cache

WORKDIR /app/EntityService
RUN gradle clean publishToMavenLocal -x test --no-daemon

# 3. Build Booking Service

WORKDIR /app/UberBookingService
RUN gradle clean bootJar -x test --no-daemon

# STAGE-2: RUN
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the final jar
COPY --from=builder /app/UberBookingService/build/libs/*.jar app.jar

EXPOSE 7777
ENTRYPOINT ["java", "-jar", "app.jar"]