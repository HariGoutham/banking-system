FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/banking-system-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]