# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
# Generate private and public keys if not present
RUN mkdir -p /build/src/main/resources/certs && \
    openssl genrsa -out /build/src/main/resources/certs/keypair.pem 2048 && \
    openssl rsa -in /build/src/main/resources/certs/keypair.pem -pubout -out /build/src/main/resources/certs/public.pem && \
    openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in /build/src/main/resources/certs/keypair.pem -out /build/src/main/resources/certs/private.pem && \
    rm /build/src/main/resources/certs/keypair.pem
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-alpine AS runtime
WORKDIR /app
COPY --from=build /build/target/ecommerce-web-app-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /build/src/main/resources/certs /app/src/main/resources/certs
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]