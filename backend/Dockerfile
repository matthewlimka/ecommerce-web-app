FROM openjdk:17-alpine
WORKDIR /app
COPY target/ecommerce-web-app-0.0.1-SNAPSHOT.jar /app/backend.jar
COPY src/main/resources/certs /app/certs
EXPOSE 9001
CMD ["java", "-jar", "/app/backend.jar"]